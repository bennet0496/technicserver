package de.beckerdd.bennet.minecraft.technicserver.technic;

import de.beckerdd.bennet.minecraft.technicserver.config.UserConfig;
import de.beckerdd.bennet.minecraft.technicserver.util.Downloader;
import de.beckerdd.bennet.minecraft.technicserver.util.Extractor;
import de.beckerdd.bennet.minecraft.technicserver.util.Logging;
import org.apache.commons.io.FileUtils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

/*
 * Created by bennet on 8/10/17.
 *
 * technicserver - run modpacks from technicpack.net as server with ease.
 * Copyright (C) 2017 Bennet Becker <bennet@becker-dd.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Class representing the Modpack inside the API
 */
public class Modpack implements Serializable {
    //region private Fields - API
    private int id;
    private String name;
    private String displayName;
    private String user;
    private URL url;
    private MinecraftVerion minecraft;
    private String version;
    private Resource icon;
    private Resource logo;
    private Solder solder;
    //endregion

    //region private Field - Pack
    private HashSet<Mod> mods;
    private HashMap<String, HashSet<String>> modFiles;
    //endregion

    private State state;
    private String buildInstalled;

    public static final long serialVersionUID = 201709051908L;

    /**
     * Parse Modpack from JSON Stream
     * @param jsonStream API JSON to Parse
     * @throws IOException Parsing Failed
     */
    public Modpack(InputStream jsonStream) throws IOException {
        state = State.NOT_INSTALLED;
        Logging.log("Initializing Modpack from JSON InputStream");
        JsonReader rdr = Json.createReader(jsonStream);
        JsonObject obj = rdr.readObject();

        initSome(obj);
        if (obj.getString("solder", "").equals("")) {
            solder = null;
            mods = null;
            Logging.logDebug("Modpack isn't using Solder API");
        } else {
            solder = new Solder(obj.getString("solder"));
            mods = new HashSet<>();
            Logging.logDebug("Modpack is using Solder API @ " + solder);
            solder.initMods(this);
        }
        modFiles = new HashMap<>();

        Logging.log("Identified Modpack '" + displayName + "' (" + name + ") by " + user);
    }

    /**
     * Initializies internal fields. called by {@link #Modpack(InputStream)}
     * @param obj JSON object to parse from.
     * @throws MalformedURLException URLs inside JSON are Malformed
     */
    private void initSome(JsonObject obj) throws MalformedURLException {
        Logging.log("Initializing Modpack from JSON Object");

        id = obj.getInt("id");
        name = obj.getString("name");
        displayName = obj.getString("displayName");
        user = obj.getString("user");

        if (!obj.getString("url", "").equals("")) {
            url = new URL(obj.getString("url"));
        }

        minecraft = new MinecraftVerion(obj.getString("minecraft"));
        version = obj.getString("version");
        icon = new Resource(obj.getJsonObject("icon"));
        logo = new Resource(obj.getJsonObject("logo"));
    }

    //region Getters
    public String getName() {
        return name;
    }

    public MinecraftVerion getMinecraft() {
        return minecraft;
    }

    public Resource getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUser() {
        return user;
    }

    public URL getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public HashMap<String, HashSet<String>> getModFiles() {
        return modFiles;
    }

    public State getState() {
        return state;
    }

    public String getBuildInstalled() {
        return buildInstalled;
    }

    public Resource getLogo() {
        return logo;
    }

    public Solder getSolder() {
        return solder;
    }

    public HashSet<Mod> getMods() {
        return mods;
    }
//endregion

    public void overrideMinecraft(String minecraft) {
        this.minecraft = new MinecraftVerion(minecraft);
    }

    /**
     * Add a mod to the internal List
     * @param mod Mod to add
     * @return this #SyntacticSugar
     */
    public Modpack addMod(Mod mod) {
        mods.add(mod);
        return this;
    }

    /**
     * Download all Modpack files (Mods)
     * @return this
     * @throws IOException Download Failed due to source or destination unavailability
     */
    public Modpack downloadAll() throws IOException {
        FileUtils.forceMkdir(new File("cache/"));

        if (solder == null) {
            if (state == State.INSTALLED_UPDATEABLE) {
                modFiles.get("package").parallelStream().forEach(fn -> {
                    try {
                        Logging.logDebug("Deleting " + fn);
                        FileUtils.forceDelete(new File(fn));
                    } catch (IOException e) {
                        e.printStackTrace();
                        Logging.logErr("could not delete " + fn);
                    }
                });
            }
            Downloader.downloadFile(url, "cache/package.zip");
            buildInstalled = version;
        } else {
            downloadAllViaSolder();
        }
        return this;
    }

    /**
     * Download the Modpack via the Solder API
     * @throws IOException Download Failed due to source or destination unavailability
     */
    private void downloadAllViaSolder() throws IOException{
        HashSet<Mod> modsToDownload;
        if (state == State.INSTALLED_UPDATEABLE) {
            modsToDownload = new HashSet<>();

            HashSet<Mod> oldMods = new HashSet<>(mods);
            mods = new HashSet<>();
            solder.initMods(this);

            HashSet<Mod> modsToClear = new HashSet<>();

            Logging.logDebug("Searching for updated or removed mods");

            // find updated mods, we need to download
            modsToDownload.addAll(oldMods.stream().filter(
                    oldMod -> mods.stream().anyMatch(
                            mod -> oldMod.getName().equals(mod.getName()) &&
                                    !oldMod.getVersion().equals(mod.getVersion()))).collect(Collectors.toSet()));
            // we need to clear updated mods too
            modsToClear.addAll(modsToDownload);

            // find mods removed, as they need to be cleared (obviously)
            modsToClear.addAll(oldMods.stream().filter(
                    oldMod -> mods.stream().noneMatch(
                            mod -> oldMod.getName().equals(mod.getName())
                    )).collect(Collectors.toSet()));

            // obviously we also need to download newly added mods
            modsToDownload.addAll(mods.stream().filter(
                    mod -> oldMods.stream().noneMatch(
                            oldMod -> oldMod.getName().equals(mod.getName())
                    )).collect(Collectors.toSet()));

            Logging.logDebug("Found " + modsToClear.size() + " updated or removed mods");
            Logging.logDebug("Starting to clean mod for update");

            modsToClear.parallelStream().forEach(modToClear -> {
                modFiles.get(modToClear.getName()).parallelStream().forEach(file -> {
                    try {
                        Logging.logDebug("Deleting " + file);
                        FileUtils.forceDelete(new File(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                        Logging.logErr("could not delete " + file + " from mod " + modToClear.getName());
                    }
                });
                // we ne to remove the mod from the fileset, as it not part of the installation anymore
                modFiles.remove(modToClear.getName());
            });
        } else {
            modsToDownload = new HashSet<>(mods);
        }

        modsToDownload.parallelStream().forEach(mod -> {
            try {
                mod.download();
            } catch (IOException e) {
                e.printStackTrace();
                Logging.logErr("Could not download mod " + mod.getName());
            }
        });
        buildInstalled = solder.parseBuild(UserConfig.getBuild(), this);
    }

    /**
     * Extract all downloaded Mod ZIPs. Must be called after {@link #downloadAll()}
     * @return this
     * @throws IOException Source File or Destination Path are unreadable/writeable
     */
    public Modpack extractAll() throws IOException {
        if (solder != null) {
            mods.parallelStream().forEach(mod -> {
                try {
                    mod.extract();
                } catch (IOException e) {
                    e.printStackTrace();
                    Logging.logErr("Failed unpacking " + mod.getName());
                }
                modFiles.put(mod.getName(), mod.getFileSet());
            });
            if (state == State.INSTALLED_UPDATEABLE) {
                mods = new HashSet<>();
                solder.initMods(this);
            }
        } else {
            modFiles.put("package", Extractor.extractZip("cache/package.zip"));
        }
        state = State.INSTALLED_UPTODATE;
        return this;
    }

    /**
     * Check and Update the internal State. Called by TechnicAPI#main(String[]) when modpack.state file found
     * @param jsonStream Current JSOn from the API
     * @throws IOException fired by Malformed URLs
     */
    public void update(InputStream jsonStream) throws IOException {
        if (state == null || state == State.NOT_INSTALLED) {
            throw new IllegalStateException();
        }

        initSome(Json.createReader(jsonStream).readObject());
        if (((solder == null) && !(buildInstalled.equals(version))) ||
                ((solder != null) && !buildInstalled.equals(solder.parseBuild(UserConfig.getBuild(), this)))) {
            state = State.INSTALLED_UPDATEABLE;
        } else {
            state = State.INSTALLED_UPTODATE;
        }
    }

    /**
     * Internal State Hack :)
     */
    public enum State implements Serializable{
        NOT_INSTALLED,
        INSTALLED_UPTODATE,
        INSTALLED_UPDATEABLE
    }
}

package de.beckerdd.bennet.minecraft.technicserver.minecraftforge.installer.transform;

import java.util.Locale;

import com.google.common.base.Strings;

import argo.jdom.JsonNode;
import de.beckerdd.bennet.minecraft.technicserver.minecraftforge.installer.Artifact;

public class TransformInfo {
    public final String side;
    public final String input;
    public final Artifact output;
    public final String map;
    public final boolean append;
    private Artifact inputArtifact;
    public final String maven;

    public TransformInfo(JsonNode node)
    {
        side     = node.isStringValue("side") ? node.getStringValue("side").toUpperCase(Locale.ENGLISH) : null;
        input    = node.getStringValue("input");
        output   = new Artifact(node.getStringValue("output"));
        map      = node.getStringValue("map");
        append   = getBool(node, "append", false);
        maven    = node.isStringValue("maven") ? node.getStringValue("maven") : null;
    }

    private boolean getBool(JsonNode node, String name, boolean _def)
    {
        if (!node.isBooleanValue(name))
            return _def;
        return node.getBooleanValue(name);
    }

    public boolean isValid()
    {
        return !Strings.isNullOrEmpty(input) && output != null && !Strings.isNullOrEmpty(map);
    }

    public Artifact getInputArtifact()
    {
        if (inputArtifact == null)
            inputArtifact = new Artifact(input);
        return inputArtifact;
    }

    public boolean validSide(String side) {
        return side == null || side.toUpperCase(Locale.ENGLISH).equals(this.side);
    }

}

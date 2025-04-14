package com.tttsaurus.ingameinfo.common.api.gui.theme;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import java.io.*;

public final class ThemeConfigSerDesUtils
{
    private static class NonClosingBufferedWriter extends BufferedWriter
    {
        public boolean closeHint = false;
        public NonClosingBufferedWriter(Writer out) { super(out); }

        @Override
        public void close() throws IOException
        {
            if (closeHint)
                super.close();
            else
                super.flush();
        }
    }

    public static String serialize(ThemeConfig config) throws IOException
    {
        StringWriter writer = new StringWriter();
        NonClosingBufferedWriter bufferedWriter = new NonClosingBufferedWriter(writer);

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .sink(() -> bufferedWriter)
                .build();

        ConfigurationNode root = loader.createNode();
        ((CommentedConfigurationNode)(root.node("version").set(ThemeConfig.latestVersion))).comment("Don't touch 'version' manually. This is an indicator for config updater.");
        root.node("config").set(config);

        loader.save(root);
        bufferedWriter.flush();

        bufferedWriter.closeHint = true;
        bufferedWriter.close();

        return writer.toString();
    }

    public static ThemeConfigUpdater deserialize(String raw) throws IOException
    {
        StringReader reader = new StringReader(raw);
        BufferedReader bufferedReader = new BufferedReader(reader);

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .source(() -> bufferedReader)
                .build();

        ConfigurationNode root = loader.load();
        bufferedReader.close();

        ObjectMapper.Factory factory = ObjectMapper.factory();
        ObjectMapper<ThemeConfig> mapper = factory.get(ThemeConfig.class);

        return new ThemeConfigUpdater(mapper.load(root.node("config")), root.node("version").getInt(), root);
    }
}

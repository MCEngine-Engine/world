package io.github.mcengine.papermc.world.engine;

import io.github.mcengine.api.core.MCEngineCoreApi;
import io.github.mcengine.api.core.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main PaperMC plugin class for the MCEngine World module.
 */
public class MCEngineWorldPaperMC extends JavaPlugin {

    /** Metrics project id for bStats. */
    private static final int METRICS_PROJECT_ID = 22578;

    /** Command root namespace for this module. */
    private static final String COMMAND_NAMESPACE = "world";

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        new Metrics(this, METRICS_PROJECT_ID);
        saveDefaultConfig(); // Save config.yml if it doesn't exist

        boolean enabled = getConfig().getBoolean("enable", false);
        if (!enabled) {
            getLogger().warning("Plugin is disabled in config.yml (enable: false). Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        final String licenseType = getConfig().getString("license", "free");
        if (!"free".equalsIgnoreCase(licenseType)) {
            getLogger().warning("License is not 'free'. Disabling MCEngineIdentity.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Load extensions (Libraries, APIs, Agents, AddOns, DLCs)
        MCEngineCoreApi.loadExtensions(
            this,
            "io.github.mcengine.api.world.extension.library.IMCEngineWorldLibrary",
            "libraries",
            "Library"
        );
        MCEngineCoreApi.loadExtensions(
            this,
            "io.github.mcengine.api.world.extension.api.IMCEngineWorldAPI",
            "apis",
            "API"
        );
        MCEngineCoreApi.loadExtensions(
            this,
            "io.github.mcengine.api.world.extension.agent.IMCEngineWorldAgent",
            "agents",
            "Agent"
        );
        MCEngineCoreApi.loadExtensions(
            this,
            "io.github.mcengine.api.world.extension.addon.IMCEngineWorldAddOn",
            "addons",
            "AddOn"
        );
        MCEngineCoreApi.loadExtensions(
            this,
            "io.github.mcengine.api.world.extension.dlc.IMCEngineWorldDLC",
            "dlcs",
            "DLC"
        );

        // Check for plugin updates (repo names updated to World)
        MCEngineCoreApi.checkUpdate(
            this,
            getLogger(),
            "github",
            "MCEngine-Engine",
            "world",
            getConfig().getString("github.token", "null")
        );
    }

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic if needed
    }
}

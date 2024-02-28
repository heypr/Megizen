package net.tickmc.megizen.bukkit.commands;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.generator.ArgDefaultNull;
import com.denizenscript.denizencore.scripts.commands.generator.ArgName;
import com.denizenscript.denizencore.scripts.commands.generator.ArgPrefixed;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.mount.controller.MountControllerTypes;
import net.tickmc.megizen.bukkit.objects.MegActiveModelTag;

public class MegMountCommand extends AbstractCommand {
    public MegMountCommand() {
        setName("megmount");
        setSyntax("megmount [entity:<entity>] [model:<active_model>] [driver/passenger]");
        autoCompile();
    }

    // <--[command]
    // @Name MegState
    // @Syntax megmount [entity:<entity>] [model:<active_model>] [driver/passenger]
    // @Required 3
    // @Short Mounts the given entity on the given modeled entity, either as a passenger or the driver.
    // @Group Megizen
    //
    // @Description
    // Mounts the given entity on the given modeled entity, either as a passenger or the driver.

    // -->

    public static void autoExecute(ScriptEntry scriptEntry,
                                   @ArgName("entity") @ArgPrefixed @ArgDefaultNull EntityTag entity,
                                   @ArgName("model") @ArgPrefixed MegActiveModelTag model,
                                   @ArgName("driver/passenger") @ArgPrefixed String string) {
        ActiveModel activeModel = model.getActiveModel();
        if (entity == null) {
            Debug.echoError("The 'entity' argument is required to mount an entity.");
            return;
        }
        if (activeModel == null) {
            Debug.echoError("The 'model' argument is required to mount an entity.");
            return;
        }
        if (!string.equalsIgnoreCase("driver") && !string.equalsIgnoreCase("passenger")) {
            Debug.echoError("The 'driver/passenger' argument is required to mount an entity.");
            return;
        }
        if (string.equalsIgnoreCase("driver")) {
            activeModel.getMountManager().ifPresent(mountManager -> {
                if (mountManager.getDriver() != null) {
                    mountManager.dismountDriver();
                }
                mountManager.mountDriver(entity.entity, MountControllerTypes.WALKING);
            });
        }
        else if (string.equalsIgnoreCase("passenger")){
            activeModel.getMountManager().ifPresent(mountManager -> {
                mountManager.mountPassenger("seat", entity.entity, MountControllerTypes.WALKING);
            });
        }

    }
}

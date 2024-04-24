package net.tickmc.megizen.bukkit.commands;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.generator.ArgDefaultNull;
import com.denizenscript.denizencore.scripts.commands.generator.ArgDefaultText;
import com.denizenscript.denizencore.scripts.commands.generator.ArgName;
import com.denizenscript.denizencore.scripts.commands.generator.ArgPrefixed;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import com.ticxo.modelengine.api.model.bone.behavior.GlobalBehaviorData;
import com.ticxo.modelengine.api.model.bone.manager.BehaviorManager;
import com.ticxo.modelengine.api.model.bone.manager.MountData;
import com.ticxo.modelengine.api.model.bone.manager.MountManager;
import com.ticxo.modelengine.api.mount.controller.MountControllerTypes;
import net.tickmc.megizen.bukkit.objects.MegActiveModelTag;
import net.tickmc.megizen.bukkit.objects.MegBoneTag;
import net.tickmc.megizen.bukkit.objects.MegModeledEntityTag;

public class MegMountCommand extends AbstractCommand {
    public MegMountCommand() {
        setName("megmount");
        setSyntax("megmount [entity:<entity>] [model:<active_model>] (driver) (passenger)");
        autoCompile();
    }

    // <--[command]
    // @Name MegState
    // @Syntax megmount [type:driver/passenger] [entity:<entity>] [model:<active_model>]
    // @Required 3
    // @Short Mounts the given entity on the given modeled entity, either as a passenger or the driver.
    // @Group Megizen
    //
    // @Description
    // Mounts the given entity on the given modeled entity, either as a passenger or the driver.

    // -->

    public static void autoExecute(ScriptEntry scriptEntry,
                                   @ArgName("entity") @ArgPrefixed EntityTag entity,
                                   @ArgName("model") @ArgPrefixed MegActiveModelTag model,
                                   @ArgName("driver") @ArgDefaultNull boolean driver,
                                   @ArgName("passenger") @ArgDefaultNull boolean passenger,
                                   @ArgName("mount_interactable") boolean interactable,
                                   @ArgName("mount_damageable") boolean damageable) {
        if (entity == null) {
            Debug.echoError("The 'entity' argument is required to mount an entity.");
            return;
        }
        if (model == null) {
            Debug.echoError("The 'model' argument is required to mount an entity.");
            return;
        }
        if (!driver && !passenger) {
            Debug.echoError("A type of 'driver' or 'passenger' must be specified.");
            return;
        }
        ActiveModel activeModel = model.getActiveModel();
        if (driver) {
            activeModel.getMountManager().ifPresent(mountManager -> {
                if (mountManager.getDriver() != null) {
                    mountManager.dismountDriver();
                }
                mountManager.mountDriver(entity.entity, MountControllerTypes.WALKING);
            });
        }
        else {
            activeModel.getMountManager().ifPresent(mountManager -> {
                mountManager.mountPassenger(mountManager.getPassengerSeatMap().forEach( ).entity, MountControllerTypes.WALKING);
            });
        }
    }
}

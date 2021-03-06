package novamachina.exnihilosequentia.common.tileentity.crucible;

import novamachina.exnihilosequentia.api.ExNihiloRegistries;
import novamachina.exnihilosequentia.common.init.ExNihiloTiles;
import novamachina.exnihilosequentia.common.utility.Config;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class WoodCrucibleTile extends BaseCrucibleTile {

    public WoodCrucibleTile() {
        super(ExNihiloTiles.CRUCIBLE_WOOD.get());
    }

    @Override
    public void tick() {
        if (level.isClientSide()) {
            return;
        }

        inventory.setCrucibleHasRoom(tank.getFluidAmount() < MAX_FLUID_AMOUNT);
        ticksSinceLast++;

        if (ticksSinceLast >= Config.getTicksBetweenMelts()) {
            ticksSinceLast = 0;

            int heat = getHeat();
            if (heat <= 0) {
                return;
            }
            if (solidAmount <= 0) {
                if (!inventory.getStackInSlot(0).isEmpty()) {
                    currentItem = inventory.getStackInSlot(0).copy();
                    inventory.getStackInSlot(0).shrink(1);

                    if (inventory.getStackInSlot(0).isEmpty()) {
                        inventory.setStackInSlot(0, ItemStack.EMPTY);
                    }

                    solidAmount = ExNihiloRegistries.CRUCIBLE_REGISTRY.findRecipe(currentItem.getItem())
                        .getAmount();
                } else {
                    return;
                }
            }

            if (!inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(0)
                .sameItem(currentItem)) {
                while (heat > solidAmount && !inventory.getStackInSlot(0).isEmpty()) {
                    solidAmount += ExNihiloRegistries.CRUCIBLE_REGISTRY.findRecipe(currentItem.getItem())
                        .getAmount();
                    inventory.getStackInSlot(0).shrink(1);

                    if (inventory.getStackInSlot(0).isEmpty()) {
                        inventory.setStackInSlot(0, ItemStack.EMPTY);
                    }
                }
            }

            if (heat > solidAmount) {
                heat = solidAmount;
            }

            if (heat > 0 && ExNihiloRegistries.CRUCIBLE_REGISTRY
                .isMeltable(currentItem.getItem(), getCrucibleType().getLevel())) {
                FluidStack fluidStack = new FluidStack(
                    ExNihiloRegistries.CRUCIBLE_REGISTRY.findRecipe(currentItem.getItem()).getResultFluid(), heat);
                int filled = tank.fill(fluidStack, FluidAction.EXECUTE);
                solidAmount -= filled;
            }
        }
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
    }

    @Override
    public int getHeat() {
        int blockHeat = ExNihiloRegistries.HEAT_REGISTRY.getHeatAmount(level.getBlockState(worldPosition.below()).getBlock());
        return Math.max(blockHeat, Config.getWoodHeatRate());
    }

    @Override
    public CrucilbeTypeEnum getCrucibleType() {
        return CrucilbeTypeEnum.WOOD;
    }

    @Override
    public int getSolidAmount() {
        if(!currentItem.isEmpty()) {
            int itemCount = inventory.getStackInSlot(0).getCount();
            return solidAmount +
                    (itemCount * ExNihiloRegistries.CRUCIBLE_REGISTRY.findRecipe(currentItem.getItem())
                            .getAmount());
        }
        return solidAmount;
    }

    @Override
    public boolean canAcceptFluidTemperature(FluidStack fluidStack) {
        return fluidStack.getFluid().getAttributes().getTemperature() <= Config.getWoodBarrelMaxTemp();
    }

}

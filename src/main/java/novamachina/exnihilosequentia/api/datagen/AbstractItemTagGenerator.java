package novamachina.exnihilosequentia.api.datagen;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import novamachina.exnihilosequentia.api.ExNihiloTags;
import novamachina.exnihilosequentia.common.item.ore.EnumOre;

public abstract class AbstractItemTagGenerator extends ItemTagsProvider {
    public AbstractItemTagGenerator(DataGenerator generator, BlockTagsProvider blockTagsProvider, String modId, ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, modId, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Item Tags: " + modId;
    }

    protected void registerOre(EnumOre ore, ExNihiloTags.OreTag tags) {
        if (ore.shouldGenerateIngot()) {
            Item ingot = ore.getIngotItem() != null ? ore.getIngotItem() : ore.getIngotRegistryItem().get();
            Item chunk = ore.getChunkItem().get();

            tag(tags.getIngotTag()).add(ingot);
            tag(Tags.Items.INGOTS).addTag(tags.getIngotTag());
            tag(tags.getOreTag()).add(chunk);
            tag(Tags.Items.ORES).addTag(tags.getOreTag());
        } else {
            Item chunk = ore.getChunkItem().get();
            tag(tags.getOreTag()).add(chunk);
            tag(Tags.Items.ORES).addTag(tags.getOreTag());
        }
    }
}

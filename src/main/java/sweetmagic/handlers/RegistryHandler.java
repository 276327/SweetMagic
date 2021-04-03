package sweetmagic.handlers;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEnd;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import sweetmagic.config.SMConfig;
import sweetmagic.event.BlockBreakEvent;
import sweetmagic.event.HasItemEvent;
import sweetmagic.event.LivingDamageEvent;
import sweetmagic.event.LivingDethEvent;
import sweetmagic.event.LivingPotionEvent;
import sweetmagic.event.SMHarvestEvent;
import sweetmagic.event.SMLoottableEvent;
import sweetmagic.event.XPPickupEvent;
import sweetmagic.init.BiomeInit;
import sweetmagic.init.BlockInit;
import sweetmagic.init.DimensionInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.layer.LayerBarrier;
import sweetmagic.init.entity.layer.LayerEffectBase;
import sweetmagic.init.entity.layer.LayerRefresh;
import sweetmagic.init.entity.layer.LayerRegen;
import sweetmagic.init.entity.layer.LayerRenderWand;
import sweetmagic.init.entity.monster.EntityArchSpider;
import sweetmagic.init.entity.monster.EntityBlazeTempest;
import sweetmagic.init.entity.monster.EntityCreeperCal;
import sweetmagic.init.entity.monster.EntityElectricCube;
import sweetmagic.init.entity.monster.EntityEnderShadow;
import sweetmagic.init.entity.monster.EntityIfritVerre;
import sweetmagic.init.entity.monster.EntitySkullFrost;
import sweetmagic.init.entity.monster.EntityWindineVerre;
import sweetmagic.init.entity.monster.EntityWitchMadameVerre;
import sweetmagic.init.tile.chest.TileGravityChest;
import sweetmagic.init.tile.chest.TileModenRack;
import sweetmagic.init.tile.chest.TileModenWallRack;
import sweetmagic.init.tile.chest.TileRattanBasket;
import sweetmagic.init.tile.chest.TileWandPedal;
import sweetmagic.init.tile.chest.TileWoodChest;
import sweetmagic.init.tile.cook.TileFermenter;
import sweetmagic.init.tile.cook.TileFlourMill;
import sweetmagic.init.tile.cook.TileFreezer;
import sweetmagic.init.tile.cook.TileJuiceMaker;
import sweetmagic.init.tile.cook.TilePlate;
import sweetmagic.init.tile.cook.TilePot;
import sweetmagic.init.tile.cook.TileStove;
import sweetmagic.init.tile.magic.TileAetherFurnace;
import sweetmagic.init.tile.magic.TileAetherHopper;
import sweetmagic.init.tile.magic.TileFlyishForer;
import sweetmagic.init.tile.magic.TileMFChanger;
import sweetmagic.init.tile.magic.TileMFChangerAdvanced;
import sweetmagic.init.tile.magic.TileMFF;
import sweetmagic.init.tile.magic.TileMFFisher;
import sweetmagic.init.tile.magic.TileMFMMTable;
import sweetmagic.init.tile.magic.TileMFMMTank;
import sweetmagic.init.tile.magic.TileMFPot;
import sweetmagic.init.tile.magic.TileMFTable;
import sweetmagic.init.tile.magic.TileMFTableAdvanced;
import sweetmagic.init.tile.magic.TileMFTank;
import sweetmagic.init.tile.magic.TileMFTankAdvanced;
import sweetmagic.init.tile.magic.TileMagiaWrite;
import sweetmagic.init.tile.magic.TileMagicBarrier;
import sweetmagic.init.tile.magic.TileParallelInterfere;
import sweetmagic.init.tile.magic.TilePedalCreate;
import sweetmagic.init.tile.magic.TileSMSpaner;
import sweetmagic.init.tile.magic.TileSpawnStone;
import sweetmagic.init.tile.magic.TileStardustCrystal;
import sweetmagic.init.tile.magic.TileStardustWish;
import sweetmagic.init.tile.magic.TileToolRepair;
import sweetmagic.init.tile.magic.TileWarpBlock;
import sweetmagic.init.tile.plant.TileAlstroemeria;
import sweetmagic.init.tile.plant.TileCleroLanp;
import sweetmagic.init.tile.plant.TileSannyFlower;
import sweetmagic.worldgen.dungen.map.MapGenDekaijyu;
import sweetmagic.worldgen.dungen.map.MapGenIdo;
import sweetmagic.worldgen.dungen.map.MapGenKutiMura;
import sweetmagic.worldgen.dungen.map.MapGenMekyu;
import sweetmagic.worldgen.dungen.map.MapGenPyramid;
import sweetmagic.worldgen.dungen.map.MapGenTogijyo;
import sweetmagic.worldgen.dungen.map.MapWitchHouse;
import sweetmagic.worldgen.dungen.piece.DekaijyuPiece;
import sweetmagic.worldgen.dungen.piece.IdoPiece;
import sweetmagic.worldgen.dungen.piece.KutiMuraPiece;
import sweetmagic.worldgen.dungen.piece.MekyuPiece;
import sweetmagic.worldgen.dungen.piece.PyramidPiece;
import sweetmagic.worldgen.dungen.piece.TogijyoPiece;
import sweetmagic.worldgen.dungen.piece.WitchHousePiece;
import sweetmagic.worldgen.gen.BonusGen;
import sweetmagic.worldgen.gen.CFlowerGen;
import sweetmagic.worldgen.gen.CemeteryGen;
import sweetmagic.worldgen.gen.CledonGen;
import sweetmagic.worldgen.gen.FlowerGardenGen;
import sweetmagic.worldgen.gen.FluitForestFlowerGen;
import sweetmagic.worldgen.gen.GeddanGen;
import sweetmagic.worldgen.gen.MoatGen;
import sweetmagic.worldgen.gen.MoonGen;
import sweetmagic.worldgen.gen.NetGen;
import sweetmagic.worldgen.gen.SMOreGen;
import sweetmagic.worldgen.gen.SMSkyIslandGen;
import sweetmagic.worldgen.gen.SnowDropGen;
import sweetmagic.worldgen.gen.StickyGen;
import sweetmagic.worldgen.gen.SugarGen;
import sweetmagic.worldgen.gen.TentoGen;
import sweetmagic.worldgen.gen.WellGen;
import sweetmagic.worldgen.gen.WitchHouseGen;
import sweetmagic.worldgen.gen.WorldVillageGen;

public class RegistryHandler {

	public static void Common(FMLPreInitializationEvent event) {

		// レジスター関連をブロック先読み(作物のため)
		BlockInit.init();
		ItemInit.init();
		BlockInit.register();
		ItemInit.register();
		PotionInit.init();
		PotionInit.setList();

		//お飾りお花
		GameRegistry.registerWorldGenerator(new SugarGen(BlockInit.sugarbell_plant, 58), 0);
		GameRegistry.registerWorldGenerator(new CledonGen(BlockInit.clerodendrum), 0);
		GameRegistry.registerWorldGenerator(new StickyGen(BlockInit.sticky_stuff_plant, 128), 0);
		GameRegistry.registerWorldGenerator(new NetGen(BlockInit.whitenet_plant, 11), 0);
		GameRegistry.registerWorldGenerator(new MoonGen(BlockInit.moonblossom_plant, 62), 0);
		GameRegistry.registerWorldGenerator(new MoonGen(BlockInit.dm_plant, 17), 0);
		GameRegistry.registerWorldGenerator(new SugarGen(BlockInit.sannyflower_plant, 21), 1);
		GameRegistry.registerWorldGenerator(new SugarGen(BlockInit.glowflower_plant, 33), 0);
		GameRegistry.registerWorldGenerator(new SugarGen(BlockInit.raspberry_plant, 61, 16), 3);
		GameRegistry.registerWorldGenerator(new FluitForestFlowerGen(), 0);
		GameRegistry.registerWorldGenerator(new FlowerGardenGen(1), 0);
		GameRegistry.registerWorldGenerator(new CFlowerGen(3), 2);
		GameRegistry.registerWorldGenerator(new SnowDropGen(BlockInit.snowdrop, 2), 0);

		GameRegistry.registerWorldGenerator(new CemeteryGen(), 1);	// 墓地生成
		CemeteryGen.initLootTable();									// ルートテーブルInit

		GameRegistry.registerWorldGenerator(new WitchHouseGen(), 1);	// 魔女の家生成
		WitchHouseGen.initLootTable();									// ルートテーブルInit

		GameRegistry.registerWorldGenerator(new WorldVillageGen(), 1);	// 村家生成
		GameRegistry.registerWorldGenerator(new TentoGen(), 1);	// テント生成

		GameRegistry.registerWorldGenerator(new MoatGen(), 0);	// 小山生成

		GameRegistry.registerWorldGenerator(new SMSkyIslandGen(), 0);	// 浮島生成

		GameRegistry.registerWorldGenerator(new WellGen(), 0);	// 井戸生成
		WellGen.initLootTable();									// ルートテーブルInit

		GameRegistry.registerWorldGenerator(new GeddanGen(), 1);	// ゲッダン生成
		GeddanGen.setLootChestA();									// ルートテーブルInit

		GameRegistry.registerWorldGenerator(new BonusGen(), 1);	// ゲッダン生成

		// 鉱石生成
		GameRegistry.registerWorldGenerator(new SMOreGen(), 0);

		// SMディメンション生成
		MapGenStructureIO.registerStructure(MapGenPyramid.Start.class, "pyramid_top");
		PyramidPiece.registerIdoPiece();

		MapGenStructureIO.registerStructure(MapGenDekaijyu.Start.class, "dekaijyu");
		DekaijyuPiece.registerIdoPiece();

		MapGenStructureIO.registerStructure(MapGenTogijyo.Start.class, "burassamu");
		TogijyoPiece.registerIdoPiece();

		MapGenStructureIO.registerStructure(MapGenKutiMura.Start.class, "kuchihatetamura");
		KutiMuraPiece.registerIdoPiece();

		MapGenStructureIO.registerStructure(MapGenMekyu.Start.class, "mekyu");
		MekyuPiece.registerIdoPiece();

		MapGenStructureIO.registerStructure(MapGenIdo.Start.class, "ido");
		IdoPiece.registerIdoPiece();

		MapGenStructureIO.registerStructure(MapWitchHouse.Start.class, "witchhouse_main");
		WitchHousePiece.registerIdoPiece();

	}

	// tileの登録
	public static void tileHandler (String MODID) {
		GameRegistry.registerTileEntity(TileSannyFlower.class, new ResourceLocation(MODID, "ParticleFrower"));
		GameRegistry.registerTileEntity(TileCleroLanp.class, new ResourceLocation(MODID, "CleroLanp"));
		GameRegistry.registerTileEntity(TileAlstroemeria.class, new ResourceLocation(MODID, "Twilight_Alstroemeria"));
		GameRegistry.registerTileEntity(TileFlourMill.class, new ResourceLocation(MODID, "FlourMill"));
		GameRegistry.registerTileEntity(TileMFChanger.class, new ResourceLocation(MODID, "MFChanger"));
		GameRegistry.registerTileEntity(TileMFTank.class, new ResourceLocation(MODID, "MFTank"));
		GameRegistry.registerTileEntity(TileMFF.class, new ResourceLocation(MODID, "MFFurnace"));
		GameRegistry.registerTileEntity(TileMFPot.class, new ResourceLocation(MODID, "MFDM"));
		GameRegistry.registerTileEntity(TileMFFisher.class, new ResourceLocation(MODID, "MFFisher"));
		GameRegistry.registerTileEntity(TileStove.class, new ResourceLocation(MODID, "Stove"));
		GameRegistry.registerTileEntity(TilePot.class, new ResourceLocation(MODID, "Pot"));
		GameRegistry.registerTileEntity(TileMFTable.class, new ResourceLocation(MODID, "MFTable"));
		GameRegistry.registerTileEntity(TileFermenter.class, new ResourceLocation(MODID, "fermenter"));
		GameRegistry.registerTileEntity(TileSpawnStone.class, new ResourceLocation(MODID, "spawnstone"));
		GameRegistry.registerTileEntity(TileJuiceMaker.class, new ResourceLocation(MODID, "juicemaker"));
		GameRegistry.registerTileEntity(TileFreezer.class, new ResourceLocation(MODID, "Freezer"));
		GameRegistry.registerTileEntity(TilePedalCreate.class, new ResourceLocation(MODID, "Pedal"));
		GameRegistry.registerTileEntity(TileAetherFurnace.class, new ResourceLocation(MODID, "AetherFurnace"));
		GameRegistry.registerTileEntity(TileParallelInterfere.class, new ResourceLocation(MODID, "ParallelInterfere"));
		GameRegistry.registerTileEntity(TileMFTableAdvanced.class, new ResourceLocation(MODID, "MFTableAdvanced"));
		GameRegistry.registerTileEntity(TileMFChangerAdvanced.class, new ResourceLocation(MODID, "MFChangerAdvanced"));
		GameRegistry.registerTileEntity(TileMFTankAdvanced.class, new ResourceLocation(MODID, "MFTankAdvanced"));
		GameRegistry.registerTileEntity(TileModenRack.class, new ResourceLocation(MODID, "ModenRack"));
		GameRegistry.registerTileEntity(TileModenWallRack.class, new ResourceLocation(MODID, "ModenWallRack"));
		GameRegistry.registerTileEntity(TilePlate.class, new ResourceLocation(MODID, "PlateRack"));
		GameRegistry.registerTileEntity(TileFlyishForer.class, new ResourceLocation(MODID, "FlyishForer"));
		GameRegistry.registerTileEntity(TileStardustCrystal.class, new ResourceLocation(MODID, "StardustCrystal"));
		GameRegistry.registerTileEntity(TileSMSpaner.class, new ResourceLocation(MODID, "SMSpaner"));
		GameRegistry.registerTileEntity(TileRattanBasket.class, new ResourceLocation(MODID, "RattanBasket"));
		GameRegistry.registerTileEntity(TileWoodChest.class, new ResourceLocation(MODID, "WoodChest"));
		GameRegistry.registerTileEntity(TileMagicBarrier.class, new ResourceLocation(MODID, "Magicbarrier"));
		GameRegistry.registerTileEntity(TileWarpBlock.class, new ResourceLocation(MODID, "WarpBlock"));
		GameRegistry.registerTileEntity(TileWandPedal.class, new ResourceLocation(MODID, "WandPedal"));
		GameRegistry.registerTileEntity(TileMFMMTank.class, new ResourceLocation(MODID, "MFTankMasterMagia"));
		GameRegistry.registerTileEntity(TileToolRepair.class, new ResourceLocation(MODID, "ToolRepair"));
		GameRegistry.registerTileEntity(TileGravityChest.class, new ResourceLocation(MODID, "GravityChest"));
		GameRegistry.registerTileEntity(TileMagiaWrite.class, new ResourceLocation(MODID, "MagiaWrite"));
		GameRegistry.registerTileEntity(TileAetherHopper.class, new ResourceLocation(MODID, "AetherHopper"));
		GameRegistry.registerTileEntity(TileMFMMTable.class, new ResourceLocation(MODID, "MMTable"));
		GameRegistry.registerTileEntity(TileStardustWish.class, new ResourceLocation(MODID, "StardustWish"));
	}

	// 草から種の追加
	public static void addSeed () {
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.rice_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.soybean), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.corn_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.azuki_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.j_radish_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.eggplant_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.lettuce_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.cabbage_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.tomato), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.onion), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.cotton_seed), 5);
	}

	// イベント登録
	public static void eventHandler (FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(event);
    	MinecraftForge.EVENT_BUS.register(new SMLoottableEvent());
    	MinecraftForge.EVENT_BUS.register(new SMHarvestEvent());
    	MinecraftForge.EVENT_BUS.register(new LivingPotionEvent());
    	MinecraftForge.EVENT_BUS.register(new LivingDamageEvent());
		MinecraftForge.EVENT_BUS.register(new HasItemEvent());
		MinecraftForge.EVENT_BUS.register(new LivingDethEvent());
		MinecraftForge.EVENT_BUS.register(new XPPickupEvent());
		MinecraftForge.EVENT_BUS.register(new BlockBreakEvent());
	}

	// レイヤー登録
	public static void layerHandler () {
		if (SMConfig.isRender) {
			LayerEffectBase.initialiseLayers(LayerBarrier::new);
			LayerEffectBase.initialiseLayers(LayerRefresh::new);
			LayerEffectBase.initialiseLayers(LayerRenderWand::new);
			LayerEffectBase.initialiseLayers(LayerRegen::new);
		}
	}

	// ディメンション初期化
	public static void dimentionHandker () {
		DimensionInit.init();
	}

	// バイオーム初期化
	public static void biomeRegster (IForgeRegistry<Biome> registry) {
        BiomeInit.init(registry);
	}

	// スポーンするバイオーム設定
	public static void setSpawnBiome () {

		// えんちちーのスポーン設定
		if (SMConfig.spawn_mob) {

			// スポーン
			for (Biome bio : ForgeRegistries.BIOMES) {

				// 特定のバイオームなら次へ
				if (checkBiome(bio)) { continue; }

				EntityRegistry.addSpawn(EntitySkullFrost.class, 25, 1, 3, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityCreeperCal.class, 25, 1, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityEnderShadow.class, 10, 1, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityElectricCube.class, 10, 0, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityBlazeTempest.class, 10, 0, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityArchSpider.class, 10, 0, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityWitchMadameVerre.class, 15, 1, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityWindineVerre.class, 10, 1, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityIfritVerre.class, 10, 1, 1, EnumCreatureType.MONSTER, bio);
			}
		}
	}

	// バイオームチェック
	public static boolean checkBiome (Biome bio) {
		return bio == Biomes.MUSHROOM_ISLAND || bio == Biomes.MUSHROOM_ISLAND_SHORE ||
				bio == Biomes.HELL || bio == Biomes.VOID || bio instanceof BiomeEnd;
	}
}

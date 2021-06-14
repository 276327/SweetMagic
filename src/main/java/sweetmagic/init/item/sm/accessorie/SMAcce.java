package sweetmagic.init.item.sm.accessorie;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IAcce;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.item.sm.eitem.SMAcceType;
import sweetmagic.init.item.sm.sweetmagic.SMItem;
import sweetmagic.util.ItemHelper;
import sweetmagic.util.WorldHelper;

public class SMAcce extends SMItem implements IAcce {

	public final int data;
	public final boolean isDup;
	public SMAcceType type;
	private static final AttributeModifier SPEED_BOOST = new AttributeModifier("speed_boost", 0.15, 0).setSaved(false);

	public SMAcce (String name, SMAcceType type, boolean isDup, int data) {
		super(name, ItemInit.magicList);
        this.setAcceType(type);
        this.isDup = isDup;
		this.data = data;
		this.setMaxStackSize(1);
	}

	/**
	 * 0 = 勇者の腕輪
	 * 1 = ウィッチ・スクロール
	 * 2 = 灼熱の宝玉
	 * 3 = 人魚の衣
	 * 4 = 血吸の指輪
	 * 5 = エメラルドピアス
	 * 6 = フォーチュンリング
	 * 7 = 夜の帳
	 * 8 = 魔術師のグローブ
	 * 9
	 */

	@Override
	public List<String> magicToolTip(List<String> toolTip) {

		switch (this.data) {
		case 0:
			toolTip.add("tip.warrior_bracelet.name");
			break;
		case 1:
			toolTip.add("tip.witch_scroll.name");
			break;
		case 2:
			toolTip.add("tip.scorching_jewel.name");
			break;
		case 3:
			toolTip.add("tip.mermaid_veil.name");
			break;
		case 4:
			toolTip.add("tip.blood_sucking_ring.name");
			break;
		case 5:
			toolTip.add("tip.emelald_pias.name");
			break;
		case 6:
			toolTip.add("tip.fortune_ring.name");
			break;
		case 7:
			toolTip.add("tip.veil_buff.name");
			break;
		case 8:
			toolTip.add("tip.varrier_pendant.name");
			break;
		case 9:
			toolTip.add("tip.magicians_grobe.name");
			break;
		case 10:
			toolTip.add("tip.magician_quillpen.name");
			break;
		case 11:
			toolTip.add("tip.gravity_pendant.name");
			break;
		}

		return toolTip;
	}

	// 効果が発動できるかどうか
	public boolean canUseEffect (World world, EntityPlayer player, ItemStack stack) {

		switch (this.data) {
		// 血吸の指輪
		case 4:
			return player.getHealth() > 1;
		// 守護のペンダント
		case 8:
			return player.getHealth() <= 6 && !player.getCooldownTracker().hasCooldown(stack.getItem());
		}

		return true;
	}

	// 常に発動したいならここで
	public void onUpdate(World world, EntityPlayer player, ItemStack stack) {

		// 発動条件を満たさないなら終了
		if (!this.canUseEffect(world, player, stack)) { return; }

		switch (this.data) {
		case 2:
			// 灼熱の宝玉
			this.scorchEffect(world, player, stack);
			break;
		case 3:
			// 人魚の衣
			this.mermaidEffect(world, player, stack);
			break;
		case 6:
			// フォーチュンリング
			player.addPotionEffect(new PotionEffect(MobEffects.LUCK, 201, 1, true, false));
			break;
		case 7:
			// 夜の帳
			player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 201, 0, true, false));
			break;
		case 8:
			// 守護の守り
			player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 1));
			player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 200, 4));
			player.getCooldownTracker().setCooldown(stack.getItem(), 12000);
		case 11:
			// アブソープペンダント
			this.gravityEffext(world, player, stack);
		}
	}

	// 灼熱の宝玉
	public boolean scorchEffect (World world, EntityPlayer player, ItemStack stack) {

		player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 201, 0, true, false));
		if (player.isBurning()) {
			player.extinguish();
		}

		// やけど状態ならやけどを除去
		this.checkDebuf(player, PotionInit.flame);

		if (player.isInLava() ) {

			player.fallDistance = 0F;

			if (!player.capabilities.isFlying && SweetMagicCore.proxy.isJumpPressed()) {
				player.motionY += 0.04825;
			}

			if (player.moveForward < 0) {
				player.motionX *= 0.1;
				player.motionZ *= 0.1;
			} else if (player.moveForward >= 0 && player.motionX * player.motionX + player.motionY * player.motionY + player.motionZ * player.motionZ < 2.75D) {
				player.motionX *= 1.875D;
				player.motionZ *= 1.875D;
			}
		}

		return true;
	}

	// 人魚の衣
	public boolean mermaidEffect (World world, EntityPlayer player, ItemStack stack) {

		// 泡状態なら泡を除去
		this.checkDebuf(player, PotionInit.babule);

		if (player.isInWater() ) {

			player.setAir(300);
			player.fallDistance = 0F;
			player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 201, 0, true, false));


			if (!player.capabilities.isFlying && SweetMagicCore.proxy.isJumpPressed()) {
				player.motionY += 0.04825;
			}

			if (player.moveForward < 0) {
				player.motionX *= 0.1;
				player.motionZ *= 0.1;
			}

			else if (player.moveForward >= 0 && player.motionX * player.motionX + player.motionY * player.motionY + player.motionZ * player.motionZ < 2.75D) {
				player.motionX *= 1.175D;
				player.motionZ *= 1.175D;
			}
		}

		return true;
	}

	// 装備品の効果
	public boolean gravityEffext(World world, EntityPlayer player, ItemStack stack) {

		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(8);

		// アイテム吸引
		List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, aabb);

		if (!itemList.isEmpty()) {
			for (EntityItem item : itemList) {
				if (ItemHelper.hasSpace(player.inventory.mainInventory, item.getItem())) {
					WorldHelper.gravitateEntityTowards(item, player.posX, player.posY, player.posZ);
				}
			}
		}

		// 経験値吸引
		List<EntityXPOrb> expList = world.getEntitiesWithinAABB(EntityXPOrb.class, aabb);
		if (!expList.isEmpty()) {
			for (EntityXPOrb xpOrb : expList) {
				WorldHelper.gravitateEntityTowards(xpOrb, player.posX, player.posY, player.posZ);
			}
		}

		// 重力状態なら重力解除
		this.checkDebuf(player, PotionInit.gravity);

		return true;
	}

	// 装備品の効果
	public boolean acceeffect(World world, EntityPlayer player, ItemStack stack) {

		switch (this.data) {
		case 0:
			player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 600, 1, true, false));
			return true;
		case 4:

			float health = player.getHealth();

			// クリエじゃなければ体力を減らす
			if (!player.isCreative()) {
				player.setHealth(health - 0.5F);
			}

			return true;
		}

		return false;
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

		String tipEntity = "";

		switch (this.data) {
		case 0:
			tipEntity = new TextComponentTranslation("entity.braveskeleton.name", new Object[0]).getFormattedText();
			break;
		case 1:
			tipEntity = new TextComponentTranslation("entity.witchmadameverre.name", new Object[0]).getFormattedText();
			break;
		case 2:
			tipEntity = new TextComponentTranslation("entity.ifrite.name", new Object[0]).getFormattedText();
			break;
		case 3:
			tipEntity = new TextComponentTranslation("entity.windine.name", new Object[0]).getFormattedText();
			break;
		case 8:
			tipEntity = new TextComponentTranslation("entity.ancientfairy.name", new Object[0]).getFormattedText();
			break;
		case 10:
			tipEntity = new TextComponentTranslation("entity.sandryon.name", new Object[0]).getFormattedText();
			break;

		}

  		if (!tipEntity.equals("")) {
  	  		String tip = new TextComponentTranslation("tip.dropboss.name", new Object[0]).getFormattedText() + " " + tipEntity;
  			tooltip.add(I18n.format(TextFormatting.GREEN + tip));
  		}

  		if (this.data == 7) {
  			String tip = new TextComponentTranslation("tip.veil_darkness.name", new Object[0]).getFormattedText();
  			tooltip.add(I18n.format(TextFormatting.GREEN + tip));
  		}
  	}

  	// デバフチェック
  	public void checkDebuf (EntityPlayer player, Potion potion) {
		if (player.isPotionActive(potion)) {
			player.removePotionEffect(potion);
		}
  	}

	@Override
	public SMAcceType getAcceType() {
		return this.type;
	}

	// アクセサリーの設定
	@Override
	public void setAcceType(SMAcceType type) {
		this.type = type;
	}

	// 効果の重複
	@Override
	public boolean isDuplication () {
		return this.isDup;
	}
}

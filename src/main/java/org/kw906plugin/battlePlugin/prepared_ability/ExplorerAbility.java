package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.kw906plugin.battlePlugin.Ability;
import java.util.HashMap;
import java.util.Map;

public class ExplorerAbility extends Ability {
    public static Map<Material, String> itemToBuffMap;
    public static Map<String, Boolean> abilityUsedMap;

    public ExplorerAbility() {
        setName("탐험가");
        setDescription("모험을 통해 얻은 각종 아이템을 사용하여 팀원들에게 이로운 효과를 줍니다.");

        itemToBuffMap = new HashMap<>();
        abilityUsedMap = new HashMap<>();

        // 아이템과 버프의 관계 설정
        itemToBuffMap.put(Material.DRAGON_EGG, "ScoreBoost");
        itemToBuffMap.put(Material.ELYTRA, "NoFallingDamage");
        itemToBuffMap.put(Material.NETHER_STAR, "Absorption");
        itemToBuffMap.put(Material.ENCHANTED_GOLDEN_APPLE, "Regeneration");
        itemToBuffMap.put(Material.HEAVY_CORE, "Strength");
        itemToBuffMap.put(Material.DEEPSLATE_EMERALD_ORE, "Haste");
        itemToBuffMap.put(Material.NETHERITE_BLOCK, "FireResistance");
        itemToBuffMap.put(Material.CONDUIT, "WaterBreathing");

        // 각 능력에 대한 사용 여부 초기화
        abilityUsedMap.put("ScoreBoost", false);
        abilityUsedMap.put("NoFallingDamage", false);
        abilityUsedMap.put("Absorption", false);
        abilityUsedMap.put("Regeneration", false);
        abilityUsedMap.put("Strength", false);
        abilityUsedMap.put("Haste", false);
        abilityUsedMap.put("FireResistance", false);
        abilityUsedMap.put("WaterBreathing", false);
    }
}

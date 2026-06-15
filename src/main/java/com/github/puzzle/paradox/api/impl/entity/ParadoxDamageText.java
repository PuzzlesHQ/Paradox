package com.github.puzzle.paradox.api.impl.entity;

import com.github.puzzle.paradox.api.interfaces.entity.IParadoxDamageText;
import finalforeach.cosmicreach.entities.DamageTextGameEntity;
import finalforeach.cosmicreach.entities.GameEntity;

public class ParadoxDamageText extends ParadoxEntity implements IParadoxDamageText {

    DamageTextGameEntity damageText;
    public ParadoxDamageText(DamageTextGameEntity damageText) {
        super(damageText);
        this.damageText = damageText;
    }

    String getText(){
        return damageText.text;
    }
    void SetText(String text){
        damageText.text = text;
    }
}

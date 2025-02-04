package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ModifiableAttributeInstance implements IAttributeInstance {
	/** The BaseAttributeMap this attributeInstance can be found in */
	private final AbstractAttributeMap attributeMap;

	/** The Attribute this is an instance of */
	private final IAttribute genericAttribute;
	private final Map<Integer, Set<AttributeModifier>> mapByOperation = Maps.<Integer, Set<AttributeModifier>> newHashMap();
	private final Map<String, Set<AttributeModifier>> mapByName = Maps.<String, Set<AttributeModifier>> newHashMap();
	private final Map<UUID, AttributeModifier> mapByUUID = Maps.<UUID, AttributeModifier> newHashMap();
	private double baseValue;
	private boolean needsUpdate = true;
	private double cachedValue;

	public ModifiableAttributeInstance(AbstractAttributeMap attributeMapIn, IAttribute genericAttributeIn) {
		this.attributeMap = attributeMapIn;
		this.genericAttribute = genericAttributeIn;
		this.baseValue = genericAttributeIn.getDefaultValue();

		for (int i = 0; i < 3; ++i) {
			this.mapByOperation.put(Integer.valueOf(i), Sets.<AttributeModifier> newHashSet());
		}
	}

	/**
	 * Get the Attribute this is an instance of
	 */
	@Override
	public IAttribute getAttribute() {
		return this.genericAttribute;
	}

	@Override
	public double getBaseValue() {
		return this.baseValue;
	}

	@Override
	public void setBaseValue(double baseValue) {
		if (baseValue != this.getBaseValue()) {
			this.baseValue = baseValue;
			this.flagForUpdate();
		}
	}

	@Override
	public Collection<AttributeModifier> getModifiersByOperation(int operation) {
		return this.mapByOperation.get(Integer.valueOf(operation));
	}

	@Override
	public Collection<AttributeModifier> getModifiers() {
		Set<AttributeModifier> set = Sets.<AttributeModifier> newHashSet();

		for (int i = 0; i < 3; ++i) {
			set.addAll(this.getModifiersByOperation(i));
		}

		return set;
	}

	@Override
	@Nullable

	/**
	 * Returns attribute modifier, if any, by the given UUID
	 */
	public AttributeModifier getModifier(UUID uuid) {
		return this.mapByUUID.get(uuid);
	}

	@Override
	public boolean hasModifier(AttributeModifier modifier) {
		return this.mapByUUID.get(modifier.getID()) != null;
	}

	@Override
	public void applyModifier(AttributeModifier modifier) {
		if (this.getModifier(modifier.getID()) != null) {
			throw new IllegalArgumentException("Modifier is already applied on this attribute!");
		} else {
			Set<AttributeModifier> set = this.mapByName.get(modifier.getName());

			if (set == null) {
				set = Sets.<AttributeModifier> newHashSet();
				this.mapByName.put(modifier.getName(), set);
			}

			((Set) this.mapByOperation.get(Integer.valueOf(modifier.getOperation()))).add(modifier);
			set.add(modifier);
			this.mapByUUID.put(modifier.getID(), modifier);
			this.flagForUpdate();
		}
	}

	protected void flagForUpdate() {
		this.needsUpdate = true;
		this.attributeMap.onAttributeModified(this);
	}

	@Override
	public void removeModifier(AttributeModifier modifier) {
		for (int i = 0; i < 3; ++i) {
			Set<AttributeModifier> set = this.mapByOperation.get(Integer.valueOf(i));
			set.remove(modifier);
		}

		Set<AttributeModifier> set1 = this.mapByName.get(modifier.getName());

		if (set1 != null) {
			set1.remove(modifier);

			if (set1.isEmpty()) {
				this.mapByName.remove(modifier.getName());
			}
		}

		this.mapByUUID.remove(modifier.getID());
		this.flagForUpdate();
	}

	@Override
	public void removeModifier(UUID p_188479_1_) {
		AttributeModifier attributemodifier = this.getModifier(p_188479_1_);

		if (attributemodifier != null) {
			this.removeModifier(attributemodifier);
		}
	}

	@Override
	public void removeAllModifiers() {
		Collection<AttributeModifier> collection = this.getModifiers();

		if (collection != null) {
			for (AttributeModifier attributemodifier : Lists.newArrayList(collection)) {
				this.removeModifier(attributemodifier);
			}
		}
	}

	@Override
	public double getAttributeValue() {
		if (this.needsUpdate) {
			this.cachedValue = this.computeValue();
			this.needsUpdate = false;
		}

		return this.cachedValue;
	}

	private double computeValue() {
		double d0 = this.getBaseValue();

		for (AttributeModifier attributemodifier : this.getAppliedModifiers(0)) {
			d0 += attributemodifier.getAmount();
		}

		double d1 = d0;

		for (AttributeModifier attributemodifier1 : this.getAppliedModifiers(1)) {
			d1 += d0 * attributemodifier1.getAmount();
		}

		for (AttributeModifier attributemodifier2 : this.getAppliedModifiers(2)) {
			d1 *= 1.0D + attributemodifier2.getAmount();
		}

		return this.genericAttribute.clampValue(d1);
	}

	private Collection<AttributeModifier> getAppliedModifiers(int operation) {
		Set<AttributeModifier> set = Sets.newHashSet(this.getModifiersByOperation(operation));

		for (IAttribute iattribute = this.genericAttribute.getParent(); iattribute != null; iattribute = iattribute.getParent()) {
			IAttributeInstance iattributeinstance = this.attributeMap.getAttributeInstance(iattribute);

			if (iattributeinstance != null) {
				set.addAll(iattributeinstance.getModifiersByOperation(operation));
			}
		}

		return set;
	}
}

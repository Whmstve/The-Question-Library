package net.whmstve.thequestionlib.general.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;

import java.util.*;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class SavedDataMap {
    private final Map<String, Object> storage = new HashMap<>();

    private static final Supplier<String> INVALID_KEY_SUPPRESSOR = () -> "_";
    private static final String RESERVED_KEY = "DataMap";

    private String sanitizeKey(String pKey) {
        return RESERVED_KEY.equals(pKey) ? INVALID_KEY_SUPPRESSOR.get() : pKey;
    }

    // ============================= PUT METHODS ============================= //

    public void putInt(String pKey, int pValue) {
        this.storage.put(sanitizeKey(pKey), pValue);
    }

    public void putLong(String pKey, long pValue) {
        this.storage.put(sanitizeKey(pKey), pValue);
    }

    public void putDouble(String pKey, double pValue) {
        this.storage.put(sanitizeKey(pKey), pValue);
    }

    public void putString(String pKey, String pValue) {
        this.storage.put(sanitizeKey(pKey), pValue);
    }

    public void putBoolean(String pKey, boolean pValue) {
        this.storage.put(sanitizeKey(pKey), pValue);
    }

    public void putUUID(String pKey, UUID pValue) {
        this.storage.put(sanitizeKey(pKey), pValue);
    }

    public void putBlockPos(String pKey, BlockPos pValue) {
        this.storage.put(sanitizeKey(pKey), pValue);
    }

    public void putByteArray(String pKey, byte[] pValue) {
        this.storage.put(sanitizeKey(pKey), pValue);
    }

    public void putIntArray(String pKey, int[] pValue) {
        this.storage.put(sanitizeKey(pKey), pValue);
    }

    public void putLongArray(String pKey, long[] pValue) {
        this.storage.put(sanitizeKey(pKey), pValue);
    }

    public <T> void putList(String pKey, TypewrittenList<T> list) {
        this.storage.put(sanitizeKey(pKey), list);
    }

    // ============================= GET METHODS ============================= //

    public boolean getBoolean(String pKey) {
        Object val = this.storage.get(pKey);
        return val instanceof Boolean ? (Boolean) val : false;
    }

    public int getInt(String pKey) {
        Object val = this.storage.get(pKey);
        return val instanceof Integer ? (Integer) val : 0;
    }

    public long getLong(String pKey) {
        Object val = this.storage.get(pKey);
        return val instanceof Long ? (Long) val : 0L;
    }

    public double getDouble(String pKey) {
        Object val = this.storage.get(pKey);
        return val instanceof Double ? (Double) val : 0.0;
    }

    public String getString(String pKey) {
        Object val = this.storage.get(pKey);
        return val instanceof String ? (String) val : "";
    }

    public UUID getUUID(String pKey) {
        Object val = this.storage.get(pKey);
        return val instanceof UUID ? (UUID) val : new UUID(0L, 0L);
    }

    public BlockPos getBlockPos(String pKey) {
        Object val = this.storage.get(pKey);
        return val instanceof BlockPos ? (BlockPos) val : BlockPos.ZERO;
    }

    public byte[] getByteArray(String pKey) {
        Object val = this.storage.get(pKey);
        return val instanceof byte[] ? (byte[]) val : new byte[0];
    }

    public int[] getIntArray(String pKey) {
        Object val = this.storage.get(pKey);
        return val instanceof int[] ? (int[]) val : new int[0];
    }

    public long[] getLongArray(String pKey) {
        Object val = this.storage.get(pKey);
        return val instanceof long[] ? (long[]) val : new long[0];
    }

    public <T> TypewrittenList<T> getList(String pKey, Class<T> clazz) {
        Object val = this.storage.get(pKey);
        if (val instanceof TypewrittenList<?> list && clazz.isAssignableFrom(list.getWrittenClass())) {
            @SuppressWarnings("unchecked")
            TypewrittenList<T> typedList = (TypewrittenList<T>) list;
            return typedList;
        }
        return new TypewrittenList<>(clazz);
    }

    // ============================= HAS METHODS ============================= //

    public boolean hasBoolean(String pKey) {
        return this.storage.get(pKey) instanceof Boolean;
    }

    public boolean hasInt(String pKey) {
        return this.storage.get(pKey) instanceof Integer;
    }

    public boolean hasLong(String pKey) {
        return this.storage.get(pKey) instanceof Long;
    }

    public boolean hasDouble(String pKey) {
        return this.storage.get(pKey) instanceof Double;
    }

    public boolean hasString(String pKey) {
        return this.storage.get(pKey) instanceof String;
    }

    public boolean hasUUID(String pKey) {
        return this.storage.get(pKey) instanceof UUID;
    }

    public boolean hasBlockPos(String pKey) {
        return this.storage.get(pKey) instanceof BlockPos;
    }

    public boolean hasByteArray(String pKey) {
        return this.storage.get(pKey) instanceof byte[];
    }

    public boolean hasIntArray(String pKey) {
        return this.storage.get(pKey) instanceof int[];
    }

    public boolean hasLongArray(String pKey) {
        return this.storage.get(pKey) instanceof long[];
    }

    public boolean hasList(String pKey) {
        return this.storage.get(pKey) instanceof TypewrittenList;
    }

    // ============================= SERIALIZATION ============================= //

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        CompoundTag dataTag = new CompoundTag();

        this.storage.forEach((key, value) -> {
            if (value instanceof Boolean b) {
                dataTag.putBoolean(key, b);
            } else if (value instanceof Integer i) {
                dataTag.putInt(key, i);
            } else if (value instanceof Long l) {
                dataTag.putLong(key, l);
            } else if (value instanceof Double d) {
                dataTag.putDouble(key, d);
            } else if (value instanceof String s) {
                dataTag.putString(key, s);
            } else if (value instanceof UUID uuid) {
                dataTag.putUUID(key, uuid);
            } else if (value instanceof BlockPos pos) {
                dataTag.put(key, NbtUtils.writeBlockPos(pos));
            } else if (value instanceof byte[] bArr) {
                dataTag.putByteArray(key, bArr);
            } else if (value instanceof int[] iArr) {
                dataTag.putIntArray(key, iArr);
            } else if (value instanceof long[] lArr) {
                dataTag.putLongArray(key, lArr);
            } else if (value instanceof TypewrittenList<?> list) {
                CompoundTag listTag = new CompoundTag();
                listTag.putString("type", list.getWrittenClass().getName());
                ListTag innerList = new ListTag();
                for (Object obj : list) {
                    if (obj instanceof String s) {
                        innerList.add(StringTag.valueOf(s));
                    } else if (obj instanceof CompoundTag cTag) {
                        innerList.add(cTag);
                    }
                    // Add more serializable types as needed
                }
                listTag.put("values", innerList);
                dataTag.put(key, listTag);
            }
        });

        tag.put(RESERVED_KEY, dataTag);
        return tag;
    }

    public void load(CompoundTag tag) {
        this.storage.clear();
        if (!tag.contains(RESERVED_KEY)) return;

        CompoundTag dataTag = tag.getCompound(RESERVED_KEY);
        for (String key : dataTag.getAllKeys()) {
            Tag t = dataTag.get(key);
            switch (Objects.requireNonNull(t).getId()) {
                case Tag.TAG_BYTE -> this.storage.put(key, ((ByteTag) t).getAsByte() != 0);
                case Tag.TAG_INT -> this.storage.put(key, ((IntTag) t).getAsInt());
                case Tag.TAG_LONG -> this.storage.put(key, ((LongTag) t).getAsLong());
                case Tag.TAG_DOUBLE -> this.storage.put(key, ((DoubleTag) t).getAsDouble());
                case Tag.TAG_STRING -> this.storage.put(key, ((StringTag) t).getAsString());
                case Tag.TAG_BYTE_ARRAY -> this.storage.put(key, ((ByteArrayTag) t).getAsByteArray());
                case Tag.TAG_INT_ARRAY -> this.storage.put(key, ((IntArrayTag) t).getAsIntArray());
                case Tag.TAG_LONG_ARRAY -> this.storage.put(key, ((LongArrayTag) t).getAsLongArray());
                case Tag.TAG_COMPOUND -> {
                    CompoundTag compound = (CompoundTag) t;
                    if (compound.contains("X") && compound.contains("Y") && compound.contains("Z")) {
                        this.storage.put(key, NbtUtils.readBlockPos(compound));
                    } else if (compound.contains("type") && compound.contains("values")) {
                        try {
                            String className = compound.getString("type");
                            Class<?> clazz = Class.forName(className);
                            ListTag values = compound.getList("values", Tag.TAG_STRING); // or COMPOUND
                            TypewrittenList<Object> list = new TypewrittenList<>((Class<Object>) clazz);
                            for (Tag item : values) {
                                if (clazz == String.class && item instanceof StringTag st) {
                                    list.add(st.getAsString());
                                } else if (item instanceof CompoundTag cTag) {
                                    list.add(cTag);
                                }
                            }
                            this.storage.put(key, list);
                        } catch (Exception ignored) {}
                    }
                }
            }
        }
    }

    // ============================= UTILITIES ============================= //

    public void remove(String key) {
        this.storage.remove(key);
    }

    public void clear() {
        this.storage.clear();
    }

    public Set<String> keySet() {
        return this.storage.keySet();
    }
}

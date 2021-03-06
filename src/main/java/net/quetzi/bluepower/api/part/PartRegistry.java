/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.api.part;

import java.lang.reflect.Constructor;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.quetzi.bluepower.init.BPItems;
import net.quetzi.bluepower.part.PartLamp;
import net.quetzi.bluepower.part.gate.GateAnd;
import net.quetzi.bluepower.part.gate.GateNot;
import net.quetzi.bluepower.references.Refs;

public class PartRegistry {
    
    private static Map<String, Entry<Class<? extends BPPart>, Object[]>> parts = new HashMap<String, Entry<Class<? extends BPPart>, Object[]>>();
    
    public static String                                                 ICON_PART;
    
    private PartRegistry() {
    
    }
    
    /**
     * Register a part
     * 
     * @param id
     *            Part identifier (same as the one returned by the part)
     * @param part
     *            Part class
     */
    public static void registerPart(String id, Class<? extends BPPart> part, Object... constructorArgs) {
    
        if (id == null || id.trim().isEmpty()) return;
        if (part == null) return;
        Entry<Class<? extends BPPart>, Object[]> e = new AbstractMap.SimpleEntry<Class<? extends BPPart>, Object[]>(part, constructorArgs);
        if (parts.containsKey(e)) return;
        
        parts.put(id, e);
    }
    
    /**
     * Creates a part from its ID
     * 
     * @param id
     *            The part ID
     * @param isMultipart
     *            Whether it's using a multipart id or not (this is usually false)
     * @return A new instance of the part or null if it couldn't be found
     */
    public static BPPart createPart(String id, boolean isMultipart) {
    
        try {
            Entry<Class<? extends BPPart>, Object[]> e = getPartData(id, isMultipart);
            if (e == null) return null;
            Class<? extends BPPart> c = e.getKey();
            Object[] args = e.getValue();
            Class<?>[] argsClasses = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++)
                argsClasses[i] = args[i].getClass();
            
            Constructor<? extends BPPart> cons = c.getConstructor(argsClasses);
            boolean wasAccessible = cons.isAccessible();
            cons.setAccessible(true);
            BPPart inst = cons.newInstance(args);
            cons.setAccessible(wasAccessible);
            
            return inst;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Creates a part from its ID
     * 
     * @param id
     *            The part ID
     * @return A new instance of the part or null if it couldn't be found
     */
    public static BPPart createPart(String id) {
    
        return createPart(id, false);
    }
    
    public static Map<String, Entry<Class<? extends BPPart>, Object[]>> getMappings() {
    
        return Collections.unmodifiableMap(parts);
    }
    
    public static List<String> getRegisteredParts() {
    
        return Collections.unmodifiableList(new ArrayList<String>(parts.keySet()));
    }
    
    /**
     * Gets the part's item from an ID
     * 
     * @param id
     *            The part's ID
     * @return An item with the part ID
     */
    public static ItemStack getItemForPart(String id) {
    
        if (parts.containsKey(id)) {
            ItemStack is = new ItemStack(BPItems.multipart);
            
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("id", id);
            
            is.setTagCompound(tag);
            
            return is;
        }
        
        return null;
    }
    
    /**
     * Gets the part id stored in the item and creates a new part with that id
     * 
     * @param is
     *            The item to get the part id from
     * @return A new instance of the part or null if it couldn't be found
     */
    public static BPPart createPartFromItem(ItemStack is) {
    
        String id = getPartIdFromItem(is);
        return createPart(id);
    }
    
    public static Entry<Class<? extends BPPart>, Object[]> getPartData(String id) {
    
        return getPartData(id, false);
    }
    
    public static Entry<Class<? extends BPPart>, Object[]> getPartData(String id, boolean isMultipart) {
    
        try {
            for (String s : parts.keySet()) {
                if (((isMultipart ? Refs.MODID + "_" + s : s)).equals(id)) {
                    try {
                        return parts.get(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static Entry<Class<? extends BPPart>, Object[]> getPartDataFromItem(ItemStack is) {
    
        String id = getPartIdFromItem(is);
        return getPartData(id);
    }
    
    /**
     * Gets the part ID stored in the item
     * 
     * @param is
     *            Item to get the part ID from
     * @return The part ID
     */
    public static String getPartIdFromItem(ItemStack is) {
    
        NBTTagCompound tag = is.getTagCompound();
        return tag.getString("id");
    }
    
    public static void init() {
    
        ICON_PART = "not";
        // Gates
        registerPart("not", GateNot.class);
        registerPart("and", GateAnd.class);
        
        // Lamps
        registerPart("lampwhite", PartLamp.class, "white", 0xFFFFFF);
    }
    
}

package com.frahhs.robbing.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class Utils {

    public static Entity findEntityByUUID(UUID uuid, World w) {
        for(Entity e : w.getEntities()) {
            if (e.getUniqueId().equals(uuid)) {
                return e;
            }
        }
        return null;
    }

    /*public static String locationToString(Location l) {
        return l.getWorld().getName() + ":" + l.getX() + ":" + l.getY() + ":" + l.getZ();
    }

    public static Location stringToLocation(String s) {
        if (s == null || s.trim() == "") {
            return null;
        }
        final String[] parts = s.split(":");
        if (parts.length == 4) {
            final World w = Bukkit.getServer().getWorld(parts[0]);
            final double x = Double.parseDouble(parts[1]);
            final double y = Double.parseDouble(parts[2]);
            final double z = Double.parseDouble(parts[3]);
            return new Location(w, x, y, z);
        }
        return null;
    }*/

    public static boolean compareLocations(Location l1, Location l2) {
        if(l1.getBlockX() == l2.getBlockX() && l1.getBlockY() == l2.getBlockY() && l1.getBlockZ() == l2.getBlockZ()) {
            return true;
        }
        return false;
    }

    public static boolean compareLocationDouble(Location l1, Location l2) {
        return l1.getX() == l2.getX() && l1.getY() == l2.getY() && l1.getZ() == l2.getZ();
    }

    /*public static String removeLastCharFromString(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String[] getResources(Class clazz, String path) {

        // gets the resource path for the jar file
        URL dirURL = clazz.getClassLoader().getResource(path);

        // create the return list of resource filenames inside path provided
        ArrayList<String> result = new ArrayList<String>();

        // get the path of the jar file
        String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file

        // decode the compiled jar for iteration
        JarFile jar = null;
        try {
            jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Robbing.getInstance().getServer().getConsoleSender().sendMessage("ERROR - getResources() - couldn't decode the Jar file to index resources.");
        } catch (IOException ex) {
            Robbing.getInstance().getServer().getConsoleSender().sendMessage("ERROR - getResources() - couldn't perform IO operations on jar file");
        }

        // gets all the elements in a jar file for iterating through
        Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar

        // iterate through and add elements inside the structures folder to the resources to be moved.
        while(entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            // check that element starts with path
            if (name.startsWith(path)) {
                String entry = name.substring(path.length() + 1);
                String last = name.substring(name.length()- 1);

                // discard if it is a directory
                if (last != File.separator){
                    // resource contains at least one character or number
                    if (entry.matches(".*[a-zA-Z0-9].*")) {
                        //getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "Found an element that starts with the correct path: " + name);
                        //getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "Chopped off just the resource name: " + entry);
                        result.add(entry);
                    }
                }
            }
        }

        // return the array of strings of filenames inside path.
        return result.toArray(new String[result.size()]);
    }

    public static String formatCustomCommand(String str, Player p) {
        String[] allSubs = StringUtils.substringsBetween(str, "{", "}");

        if(allSubs == null)
            return str;

        Map<String, String> randomToSubtitute = new HashMap<>();
        for(String cur : allSubs) {
            if(cur.startsWith("randomInteger")) {
                Random rand = new Random();
                int givenNumber;

                String rangeString = StringUtils.substringBetween(cur,"(",")");
                String [] rangeList = rangeString.split(",");
                int start = Integer.parseInt(rangeList[0]);
                int end = Integer.parseInt(rangeList[1]);

                if(start == end)
                    givenNumber = start;
                else if(start < end)
                    givenNumber = rand.nextInt((end - start) + 1) + start;
                else
                    givenNumber = rand.nextInt((start - end) + 1) + start;

                randomToSubtitute.put("{"+ cur +"}", Integer.toString(givenNumber));
            }
        }
        str = str.replace("{player}", p.getName());
        for(String curKey : randomToSubtitute.keySet()) {
            str = str.replace(curKey, randomToSubtitute.get(curKey));
        }

        return str;
    }*/
}

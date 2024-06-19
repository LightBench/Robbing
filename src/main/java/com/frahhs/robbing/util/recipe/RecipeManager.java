package com.frahhs.robbing.util.recipe;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.item.RobbingItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeManager {
    private Robbing plugin;
    private Connection connection;

    public RecipeManager(Robbing plugin) {
        this.plugin = plugin;
        this.connection = plugin.getRobbingDatabase().getConnection();
    }

    public void saveRecipe(RobbingItem item, ShapedRecipe recipe) throws SQLException {
        Robbing.getRobbingLogger().fine("Saving %s shaped recipe", item.getName());

        String sql = "REPLACE INTO ShapedRecipe (item, pattern, ingredients) VALUES (?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, item.getName());
        pstmt.setString(2, String.join(";", recipe.getShape()));

        StringBuilder ingredients = new StringBuilder();
        for (Map.Entry<Character, ItemStack> entry : recipe.getIngredientMap().entrySet()) {
            if (entry.getValue() != null) {
                ingredients .append(entry.getKey()).append(":")
                            .append(entry.getValue().getType()).append(";");
            }
        }
        pstmt.setString(3, ingredients.toString());
        pstmt.executeUpdate();
        pstmt.close();
    }

    public ShapedRecipe loadRecipe(RobbingItem item) throws SQLException {
        Robbing.getRobbingLogger().fine("Loading %s shaped recipe", item.getName());

        String sql = "SELECT * FROM ShapedRecipe WHERE item = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, item.getName());
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            // Deserialize the pattern and ingredients
            String[] shape = rs.getString("pattern").split(";");
            Map<Character, ItemStack> ingredientMap = new HashMap<>();
            String[] ingredients = rs.getString("ingredients").split(";");
            for (String ingredient : ingredients) {
                String[] entry = ingredient.split(":");
                ingredientMap.put(entry[0].charAt(0), new ItemStack(Material.matchMaterial(entry[1])));
            }

            // Now construct the NamespacedKey for the recipe
            NamespacedKey key = item.getNamespacedKey();

            // Create the ShapedRecipe and set its components
            ShapedRecipe recipe = new ShapedRecipe(key, item.getItemStack());
            recipe.shape(shape);
            for (Map.Entry<Character, ItemStack> entry : ingredientMap.entrySet()) {
                if (entry.getValue() != null) {
                    recipe.setIngredient(entry.getKey(), entry.getValue().getType());
                }
            }

            // Close resources
            rs.close();
            pstmt.close();
            return recipe;
        }

        // Close resources
        rs.close();
        pstmt.close();
        return null;
    }


    public boolean isRecipePresent(RobbingItem item) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ShapedRecipe WHERE item = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, item.getName());
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        boolean exists = rs.getInt(1) > 0;
        rs.close();
        pstmt.close();
        return exists;
    }
}

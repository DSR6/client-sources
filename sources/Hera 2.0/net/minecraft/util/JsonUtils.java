/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonUtils
/*     */ {
/*     */   public static boolean isString(JsonObject p_151205_0_, String p_151205_1_) {
/*  16 */     return !isJsonPrimitive(p_151205_0_, p_151205_1_) ? false : p_151205_0_.getAsJsonPrimitive(p_151205_1_).isString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isString(JsonElement p_151211_0_) {
/*  24 */     return !p_151211_0_.isJsonPrimitive() ? false : p_151211_0_.getAsJsonPrimitive().isString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isBoolean(JsonObject p_180199_0_, String p_180199_1_) {
/*  29 */     return !isJsonPrimitive(p_180199_0_, p_180199_1_) ? false : p_180199_0_.getAsJsonPrimitive(p_180199_1_).isBoolean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isJsonArray(JsonObject p_151202_0_, String p_151202_1_) {
/*  37 */     return !hasField(p_151202_0_, p_151202_1_) ? false : p_151202_0_.get(p_151202_1_).isJsonArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isJsonPrimitive(JsonObject p_151201_0_, String p_151201_1_) {
/*  46 */     return !hasField(p_151201_0_, p_151201_1_) ? false : p_151201_0_.get(p_151201_1_).isJsonPrimitive();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasField(JsonObject p_151204_0_, String p_151204_1_) {
/*  54 */     return (p_151204_0_ == null) ? false : ((p_151204_0_.get(p_151204_1_) != null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getString(JsonElement p_151206_0_, String p_151206_1_) {
/*  63 */     if (p_151206_0_.isJsonPrimitive())
/*     */     {
/*  65 */       return p_151206_0_.getAsString();
/*     */     }
/*     */ 
/*     */     
/*  69 */     throw new JsonSyntaxException("Expected " + p_151206_1_ + " to be a string, was " + toString(p_151206_0_));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getString(JsonObject p_151200_0_, String p_151200_1_) {
/*  78 */     if (p_151200_0_.has(p_151200_1_))
/*     */     {
/*  80 */       return getString(p_151200_0_.get(p_151200_1_), p_151200_1_);
/*     */     }
/*     */ 
/*     */     
/*  84 */     throw new JsonSyntaxException("Missing " + p_151200_1_ + ", expected to find a string");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getString(JsonObject p_151219_0_, String p_151219_1_, String p_151219_2_) {
/*  94 */     return p_151219_0_.has(p_151219_1_) ? getString(p_151219_0_.get(p_151219_1_), p_151219_1_) : p_151219_2_;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getBoolean(JsonElement p_151216_0_, String p_151216_1_) {
/* 103 */     if (p_151216_0_.isJsonPrimitive())
/*     */     {
/* 105 */       return p_151216_0_.getAsBoolean();
/*     */     }
/*     */ 
/*     */     
/* 109 */     throw new JsonSyntaxException("Expected " + p_151216_1_ + " to be a Boolean, was " + toString(p_151216_0_));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getBoolean(JsonObject p_151212_0_, String p_151212_1_) {
/* 118 */     if (p_151212_0_.has(p_151212_1_))
/*     */     {
/* 120 */       return getBoolean(p_151212_0_.get(p_151212_1_), p_151212_1_);
/*     */     }
/*     */ 
/*     */     
/* 124 */     throw new JsonSyntaxException("Missing " + p_151212_1_ + ", expected to find a Boolean");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getBoolean(JsonObject p_151209_0_, String p_151209_1_, boolean p_151209_2_) {
/* 134 */     return p_151209_0_.has(p_151209_1_) ? getBoolean(p_151209_0_.get(p_151209_1_), p_151209_1_) : p_151209_2_;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getFloat(JsonElement p_151220_0_, String p_151220_1_) {
/* 143 */     if (p_151220_0_.isJsonPrimitive() && p_151220_0_.getAsJsonPrimitive().isNumber())
/*     */     {
/* 145 */       return p_151220_0_.getAsFloat();
/*     */     }
/*     */ 
/*     */     
/* 149 */     throw new JsonSyntaxException("Expected " + p_151220_1_ + " to be a Float, was " + toString(p_151220_0_));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getFloat(JsonObject p_151217_0_, String p_151217_1_) {
/* 158 */     if (p_151217_0_.has(p_151217_1_))
/*     */     {
/* 160 */       return getFloat(p_151217_0_.get(p_151217_1_), p_151217_1_);
/*     */     }
/*     */ 
/*     */     
/* 164 */     throw new JsonSyntaxException("Missing " + p_151217_1_ + ", expected to find a Float");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getFloat(JsonObject p_151221_0_, String p_151221_1_, float p_151221_2_) {
/* 174 */     return p_151221_0_.has(p_151221_1_) ? getFloat(p_151221_0_.get(p_151221_1_), p_151221_1_) : p_151221_2_;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getInt(JsonElement p_151215_0_, String p_151215_1_) {
/* 183 */     if (p_151215_0_.isJsonPrimitive() && p_151215_0_.getAsJsonPrimitive().isNumber())
/*     */     {
/* 185 */       return p_151215_0_.getAsInt();
/*     */     }
/*     */ 
/*     */     
/* 189 */     throw new JsonSyntaxException("Expected " + p_151215_1_ + " to be a Int, was " + toString(p_151215_0_));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getInt(JsonObject p_151203_0_, String p_151203_1_) {
/* 198 */     if (p_151203_0_.has(p_151203_1_))
/*     */     {
/* 200 */       return getInt(p_151203_0_.get(p_151203_1_), p_151203_1_);
/*     */     }
/*     */ 
/*     */     
/* 204 */     throw new JsonSyntaxException("Missing " + p_151203_1_ + ", expected to find a Int");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getInt(JsonObject p_151208_0_, String p_151208_1_, int p_151208_2_) {
/* 214 */     return p_151208_0_.has(p_151208_1_) ? getInt(p_151208_0_.get(p_151208_1_), p_151208_1_) : p_151208_2_;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonObject getJsonObject(JsonElement p_151210_0_, String p_151210_1_) {
/* 223 */     if (p_151210_0_.isJsonObject())
/*     */     {
/* 225 */       return p_151210_0_.getAsJsonObject();
/*     */     }
/*     */ 
/*     */     
/* 229 */     throw new JsonSyntaxException("Expected " + p_151210_1_ + " to be a JsonObject, was " + toString(p_151210_0_));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonObject getJsonObject(JsonObject base, String key) {
/* 235 */     if (base.has(key))
/*     */     {
/* 237 */       return getJsonObject(base.get(key), key);
/*     */     }
/*     */ 
/*     */     
/* 241 */     throw new JsonSyntaxException("Missing " + key + ", expected to find a JsonObject");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonObject getJsonObject(JsonObject p_151218_0_, String p_151218_1_, JsonObject p_151218_2_) {
/* 251 */     return p_151218_0_.has(p_151218_1_) ? getJsonObject(p_151218_0_.get(p_151218_1_), p_151218_1_) : p_151218_2_;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonArray getJsonArray(JsonElement p_151207_0_, String p_151207_1_) {
/* 260 */     if (p_151207_0_.isJsonArray())
/*     */     {
/* 262 */       return p_151207_0_.getAsJsonArray();
/*     */     }
/*     */ 
/*     */     
/* 266 */     throw new JsonSyntaxException("Expected " + p_151207_1_ + " to be a JsonArray, was " + toString(p_151207_0_));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonArray getJsonArray(JsonObject p_151214_0_, String p_151214_1_) {
/* 275 */     if (p_151214_0_.has(p_151214_1_))
/*     */     {
/* 277 */       return getJsonArray(p_151214_0_.get(p_151214_1_), p_151214_1_);
/*     */     }
/*     */ 
/*     */     
/* 281 */     throw new JsonSyntaxException("Missing " + p_151214_1_ + ", expected to find a JsonArray");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonArray getJsonArray(JsonObject p_151213_0_, String p_151213_1_, JsonArray p_151213_2_) {
/* 291 */     return p_151213_0_.has(p_151213_1_) ? getJsonArray(p_151213_0_.get(p_151213_1_), p_151213_1_) : p_151213_2_;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(JsonElement p_151222_0_) {
/* 299 */     String s = StringUtils.abbreviateMiddle(String.valueOf(p_151222_0_), "...", 10);
/*     */     
/* 301 */     if (p_151222_0_ == null)
/*     */     {
/* 303 */       return "null (missing)";
/*     */     }
/* 305 */     if (p_151222_0_.isJsonNull())
/*     */     {
/* 307 */       return "null (json)";
/*     */     }
/* 309 */     if (p_151222_0_.isJsonArray())
/*     */     {
/* 311 */       return "an array (" + s + ")";
/*     */     }
/* 313 */     if (p_151222_0_.isJsonObject())
/*     */     {
/* 315 */       return "an object (" + s + ")";
/*     */     }
/*     */ 
/*     */     
/* 319 */     if (p_151222_0_.isJsonPrimitive()) {
/*     */       
/* 321 */       JsonPrimitive jsonprimitive = p_151222_0_.getAsJsonPrimitive();
/*     */       
/* 323 */       if (jsonprimitive.isNumber())
/*     */       {
/* 325 */         return "a number (" + s + ")";
/*     */       }
/*     */       
/* 328 */       if (jsonprimitive.isBoolean())
/*     */       {
/* 330 */         return "a boolean (" + s + ")";
/*     */       }
/*     */     } 
/*     */     
/* 334 */     return s;
/*     */   }
/*     */ }


/* Location:              C:\Users\mymon\AppData\Roaming\.minecraft\versions\Hera\Hera.jar!\net\minecraf\\util\JsonUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
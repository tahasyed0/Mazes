package me.name.mazes.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.name.mazes.Mazes;
import org.bukkit.Location;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class LocationTypeAdapter extends TypeAdapter<Location> {
    @Override
    public void write(JsonWriter writer, Location location) throws IOException {
        writer.beginArray();
        Map<String, Object> loc = location.serialize();
        String serialized = Mazes.gson.toJson(loc);
        writer.value(serialized);
        writer.endArray();
    }

    @Override
    public Location read(JsonReader reader) throws IOException {
        reader.beginArray();
        String serialized = reader.nextString();
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> loc = Mazes.gson.fromJson(serialized, type);
        reader.endArray();
        return Location.deserialize(loc);
    }
}







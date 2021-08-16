package me.codexadrian;

import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Generator {
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws IOException {
        //Gets the names of all the textures in the "src/main/resources/blocks" folder and adds them to a List
        List<String> pathNames;
        File file = new File("src/main/resources/blocks");
        pathNames = Arrays.stream(Objects.requireNonNull(file.list())).toList();
        assert pathNames != null;
        //removes the .png from the name
        pathNames = pathNames.stream().map(s -> s.substring(0, s.length() - 4)).toList();

        //For every png, generate an mcmeta file
        for (String pathName : pathNames) {
            System.out.println(pathName);
            String newFile = "src/main/resources/mcmeta/" + pathName + ".mcmeta";
            MCMeta mcMeta = new MCMeta();
            //Checks if it's a pillar, and changes the type accordingly
            mcMeta.type = pathName.contains("pillar") ? "pillar" : "edges";
            mcMeta.textures = new String[]{"chipped:blocks/" + pathName};
            try {
                File mcMetaFile = new File(newFile);
                if (mcMetaFile.createNewFile()) {
                    System.out.println("File created: " + mcMetaFile.getName());
                } else {
                    System.out.println("File already exists.");
                }
                try {
                    //Adds root directory
                    JsonElement root = gson.toJsonTree(mcMeta);
                    DefaultExtra extra = new DefaultExtra();
                    root.getAsJsonObject().add("extra", gson.toJsonTree(extra));
                    JsonObject finalJson = new JsonObject();
                    finalJson.add("ctm", root);
                    //Creates and writes to file with prettified content
                    FileWriter mcMetaWriter = new FileWriter(newFile);
                    System.out.println(gson.toJson(finalJson));
                    mcMetaWriter.write(gson.toJson(finalJson));
                    mcMetaWriter.close();
                } catch (IOException e) {
                    System.out.println("an error occured");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

        }
    }
}

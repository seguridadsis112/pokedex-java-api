package eu.iamgio.pokedex.machines;

import com.google.gson.JsonObject;
import eu.iamgio.pokedex.connection.HttpConnection;
import eu.iamgio.pokedex.exception.PokedexException;
import eu.iamgio.pokedex.util.NamedResource;
import eu.iamgio.pokedex.util.StringUtil;
import eu.iamgio.pokedex.version.VersionGroup;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Representation of items that teach moves to Pokémon. They vary from version to version, so it is not certain that one specific TM or HM corresponds to a single Machine
 * @author Gio
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Machine {

    /**
     * The identifier for this machine
     */
    private int id;

    /**
     * TODO make Item
     * The TM or HM item that corresponds to this machine
     */
    private String item;

    /**
     * The move that is taught by this machine
     */
    private String moveName;

    /**
     * The version group that this machine applies to
     */
    private VersionGroup versionGroup;

    /**
     * @param name Name of the machine
     * @return Machine whose name matches <tt>name</tt>
     * @throws PokedexException if <tt>name</tt> doesn't match a machine name
     */
    public static Machine fromName(String name) throws PokedexException {
        JsonObject json;
        try {
            json = new HttpConnection("machine/" + name + "/").getJson();
        } catch(RuntimeException e) {
            throw new PokedexException("Could not find machine with name/ID " + name);
        }
        return new Machine(
                json.get("id").getAsInt(),
                new NamedResource(json.getAsJsonObject("item")).getName(),
                new NamedResource(json.getAsJsonObject("move")).getName(),
                VersionGroup.valueOf(StringUtil.toEnumName(new NamedResource(json.getAsJsonObject("version-group")).getName()))
        );
    }

    /**
     * @param id Identifier of the machine
     * @return Machine whose ID matches <tt>id</tt>
     * @throws PokedexException if <tt>id</tt> is 0 or less or doesn't match a machine ID
     */
    public static Machine fromId(Number id) throws PokedexException {
        return fromName(String.valueOf(id));
    }
}
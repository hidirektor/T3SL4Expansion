package me.t3sl4.expansion.askyblock;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.CoopPlay;
import com.wasteofplastic.askyblock.Settings;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class ASkyBlockExpansion extends PlaceholderExpansion implements Cacheable {
    private ASkyBlock askyblock;

    public boolean canRegister() {
        return (Bukkit.getPluginManager().getPlugin(getRequiredPlugin()) != null);
    }

    public boolean register() {
        this.askyblock = ASkyBlock.getPlugin();
        return (this.askyblock != null) ? super.register() : false;
    }

    public String getAuthor() {
        return "clip";
    }

    public String getIdentifier() {
        return "askyblock";
    }

    public String getRequiredPlugin() {
        return "ASkyBlock";
    }

    public String getVersion() {
        return "2.0";
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        if (this.askyblock != null) {
            String[] parts = identifier.split("_");
            if (parts.length > 0) {
                String key = parts[0];
                if (key.equalsIgnoreCase("top")) {
                    if (parts.length == 1) {
                        Integer playerTop = getPlayerTop(player);
                        if (playerTop != null)
                            return String.valueOf(playerTop);
                        return "";
                    }
                    if (parts.length == 3) {
                        String data = parts[1];
                        String textRank = parts[2];
                        if (NumberUtils.isNumber(textRank)) {
                            int rank = Integer.valueOf(textRank).intValue();
                            Map.Entry<UUID, Long> entry = getEntryTopIslandOwner(rank - 1);
                            if (entry != null) {
                                if (data.equalsIgnoreCase("level"))
                                    return String.valueOf(entry.getValue());
                                if (data.equalsIgnoreCase("name")) {
                                    UUID ownerID = entry.getKey();
                                    OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerID);
                                    if (owner != null)
                                        return owner.getName();
                                }
                            }
                            return "";
                        }
                    }
                }
            }
            if (player != null) {
                UUID playerID = player.getUniqueId();
                double level = this.askyblock.getPlayers().getIslandLevel(playerID);
                String number = String.format("%.2f", (level / 1000));
                String number2 = String.format("%.2f", (level/1000000));
                String number3 = String.format("%.2f", (level/1000000000));
                if (identifier.equalsIgnoreCase("level"))
                    return String.valueOf(level);
                if (identifier.equalsIgnoreCase("level1"))
                    if(level < 1000) {
                        return String.valueOf(level);
                    } else if(level > 999 && level <1000000) {
                        return String.valueOf(number + " Bin");
                    } else if(level > 999999 && level < 1000000000) {
                        return String.valueOf(number2 + " Mn.");
                    } else {
                        return String.valueOf(number3 + " Mr.");
                    }
                if (identifier.equalsIgnoreCase("level2"))
                    if(level < 1000) {
                        return String.valueOf(level);
                    } else if(level > 999 && level <1000000) {
                        return String.valueOf(number + " Bin");
                    } else if(level > 999999 && level < 1000000000) {
                        return String.valueOf(number2 + " Milyon");
                    } else {
                        return String.valueOf(number3 + " Milyar");
                    }
                if (identifier.equalsIgnoreCase("level3"))
                    if(level < 1000) {
                        return String.valueOf(level);
                    } else if(level > 999 && level <1000000) {
                        return String.valueOf(number + " K");
                    } else if(level > 999999 && level < 1000000000) {
                        return String.valueOf(number2 + " M");
                    } else {
                        return String.valueOf(number3 + " B");
                    }
                if (identifier.equalsIgnoreCase("has_island"))
                    return this.askyblock.getPlayers().hasIsland(playerID) ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
                if (identifier.equalsIgnoreCase("team_size")) {
                    List<UUID> members = this.askyblock.getPlayers().getMembers(playerID);
                    int size = (members != null) ? members.size() : 0;
                    return String.valueOf(size);
                }
                if (identifier.equalsIgnoreCase("coop_islands")) {
                    Set<Location> coopIslands = CoopPlay.getInstance().getCoopIslands(player);
                    int size = (coopIslands != null) ? coopIslands.size() : 0;
                    return String.valueOf(size);
                }
                if (identifier.equalsIgnoreCase("owner")) {
                    Location location = player.getLocation();
                    UUID ownerID = this.askyblock.getPlayers().getPlayerFromIslandLocation(location);
                    if (ownerID != null) {
                        OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerID);
                        if (owner != null)
                            return owner.getName();
                    }
                    return "";
                }
                if (identifier.equalsIgnoreCase("team_leader")) {
                    UUID leaderID = this.askyblock.getPlayers().getTeamLeader(playerID);
                    if (leaderID != null) {
                        OfflinePlayer leader = Bukkit.getOfflinePlayer(leaderID);
                        if (leader != null)
                            return leader.getName();
                    }
                    return "";
                }
                if (identifier.equalsIgnoreCase("has_team"))
                    return this.askyblock.getPlayers().inTeam(playerID) ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
                if (parts.length > 0) {
                    String key = parts[0];
                    if (key.equalsIgnoreCase("island")) {
                        Location location = this.askyblock.getPlayers().getIslandLocation(playerID);
                        if (location != null &&
                                parts.length > 1) {
                            String data = parts[1];
                            if (data.equalsIgnoreCase("x"))
                                return String.valueOf(location.getBlockX());
                            if (data.equalsIgnoreCase("y"))
                                return String.valueOf(location.getBlockY());
                            if (data.equalsIgnoreCase("z"))
                                return String.valueOf(location.getBlockZ());
                        }
                    } else if (key.equalsIgnoreCase("members") &&
                            parts.length > 1) {
                        String data = parts[1];
                        if (data.equalsIgnoreCase("max")) {
                            int maxTeam = Settings.maxTeamSize;
                            if (!player.hasPermission("askyblock.team.maxsize.*"))
                                for (PermissionAttachmentInfo perms : player.getEffectivePermissions()) {
                                    if (perms.getPermission().startsWith("askyblock.team.maxsize.")) {
                                        String[] components = perms.getPermission().split("askyblock.team.maxsize.");
                                        if (components.length > 1) {
                                            String textValue = components[1];
                                            if (NumberUtils.isNumber(textValue))
                                                maxTeam = Math.max(maxTeam, Integer.valueOf(textValue).intValue());
                                        }
                                    }
                                }
                            if (maxTeam < 1)
                                maxTeam = 1;
                            return String.valueOf(maxTeam);
                        }
                        if (data.equalsIgnoreCase("online")) {
                            if (this.askyblock.getPlayers().inTeam(playerID)) {
                                List<UUID> members = this.askyblock.getPlayers().getMembers(playerID);
                                int count = 0;
                                if (members != null)
                                    for (UUID member : members) {
                                        Player online = Bukkit.getPlayer(member);
                                        if (online != null)
                                            count++;
                                    }
                                return String.valueOf(count);
                            }
                            return String.valueOf(1);
                        }
                    }
                }
            }
        }
        return null;
    }

    public void clear() {
        this.askyblock = null;
    }

    private final Map<UUID, Long> getMapIslandOwner() {
        Map<UUID, Long> mapIslandOwner = new HashMap<>();
        for (UUID ownerID : this.askyblock.getGrid().getOwnershipMap().keySet()) {
            long level = this.askyblock.getPlayers().getIslandLevel(ownerID);
            mapIslandOwner.put(ownerID, Long.valueOf(level));
        }
        return mapIslandOwner;
    }

    private final Integer getPlayerTop(Player player) {
        if (player != null) {
            UUID playerID = player.getUniqueId();
            List<Map.Entry<UUID, Long>> listSortIslandOwner = getSortIslandOwner();
            for (int index = 0; index < listSortIslandOwner.size(); index++) {
                Map.Entry<UUID, Long> entry = listSortIslandOwner.get(index);
                UUID entryOwnerID = entry.getKey();
                if (entryOwnerID.equals(playerID))
                    return Integer.valueOf(index + 1);
            }
        }
        return null;
    }

    private final Map.Entry<UUID, Long> getEntryTopIslandOwner(int index) {
        List<Map.Entry<UUID, Long>> listSortIslandOwner = getSortIslandOwner();
        return (index < listSortIslandOwner.size()) ? listSortIslandOwner.get(index) : null;
    }

    private final List<Map.Entry<UUID, Long>> getSortIslandOwner() {
        return sortMap(getMapIslandOwner());
    }

    private final List<Map.Entry<UUID, Long>> sortMap(Map<UUID, Long> unsortMap) {
        return sortMap(unsortMap, true);
    }

    private final List<Map.Entry<UUID, Long>> sortMap(Map<UUID, Long> unsortMap, final boolean ascend) {
        if (unsortMap != null) {
            List<Map.Entry<UUID, Long>> list = new LinkedList<>(unsortMap.entrySet());
            Comparator<Map.Entry<UUID, Long>> comparator = new Comparator<Map.Entry<UUID, Long>>() {
                public int compare(Map.Entry<UUID, Long> map1, Map.Entry<UUID, Long> map2) {
                    Long value1 = map1.getValue();
                    Long value2 = map2.getValue();
                    return ascend ? value1.compareTo(value2) : value2.compareTo(value1);
                }
            };
            Collections.sort(list, comparator);
            return list;
        }
        return null;
    }
}

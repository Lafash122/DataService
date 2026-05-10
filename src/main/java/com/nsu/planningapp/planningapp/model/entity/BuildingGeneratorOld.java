package com.nsu.planningapp.planningapp.model.entity;

import com.nsu.planningapp.planningapp.model.utils.DatabaseUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Deprecated
public class BuildingGeneratorOld {
    private final Random random = new Random();
    private final List<Long> settlementsIds;
    private final List<Long> buildingsIds;

    public BuildingGeneratorOld(Connection conn) throws SQLException, IOException {
        this.settlementsIds = DatabaseUtils.fetchIds(conn, "SETTLEMENTS", "id");
        this.buildingsIds = DatabaseUtils.fetchIds(conn, "BUILDING_BLUEPRINTS", "id");
    }

    public BuildingPair generateOneBuilding() throws Exception {
        Long settlementId = getRandomElement(settlementsIds);
        Long buildingId = getRandomElement(buildingsIds);

        return new BuildingPair(settlementId, buildingId);
    }

    public Set<BuildingPair> generateUniqueSet(int count) throws Exception {
        Set<BuildingPair> pairs = new LinkedHashSet<>();
        while(pairs.size() < count) {
            pairs.add(generateOneBuilding());
        }
        return pairs;
    }

    public List<BuildingPair> generateList(int count, boolean allowDublicates) throws Exception {
        if (allowDublicates) {
            List<BuildingPair> names = new ArrayList<>();
            while (names.size() < count) {
                names.add(generateOneBuilding());
            }
            return names;
        } else {
            return new ArrayList<>(generateUniqueSet(count));
        }
    }

    private Long getRandomElement(List<Long> list) throws Exception {
        if (list.isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        return list.get(random.nextInt(list.size()));
    }
}

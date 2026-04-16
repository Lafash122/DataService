package managers;

public class DataInfoManager implements DataInfoListener {

	//ДОПИЛ ПОД БД
	@Override
	public String[] getReadyCategories() {
		return new String[] { "Resources", "Buildings" };
	}

	//ДОПИЛ ПОД БД
	@Override
	public String[] getReadyRequestsInfo(String category) {
		if ("Resources".equals(category))
			return new String[] {
				"The maximum achievable production volume",
				"The maximum achievable volume of consumption by industries",
				"The maximum possible storage capacity",
				"The amount of resources and/or workdays for building construction",
				"The amount of resources and/or workdays required to create a vehicle",
				"The number of days to fill all available storage facilities",
				"The maximum amount of storage in the warehouse of the building of internal consumption"
			};
		else if ("Buildings".equals(category))
			return new String[] {
				"Total number of living spaces",
				"The list of buildings of the specified type",
				"The number of jobs",
				"The list or number of all buildings",
				"The number of parking spaces for official vehicles of any buildings"
			};
		else
			return new String[] {};
	}
}
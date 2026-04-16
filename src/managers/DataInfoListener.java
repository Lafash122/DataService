package managers;

public interface DataInfoListener {
	String[] getReadyCategories();
	String[] getReadyRequestsInfo(String category);
}
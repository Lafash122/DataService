package types;

public record RequestInfoMsg(
		int number,
		String category,
		String requestInfo,
		VariableFields[] requestFields) {}
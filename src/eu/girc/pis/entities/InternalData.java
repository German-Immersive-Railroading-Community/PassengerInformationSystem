package eu.girc.pis.entities;

import java.util.Set;
import java.util.TreeSet;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"generateIds"})
public class InternalData {
	
	private final Set<Integer> generatedIds = new TreeSet<>();
	
	@JsonCreator
	public InternalData(@JsonProperty("generatedIds") Set<Integer> generatedIds) {
		this.generatedIds.addAll(generatedIds);
	}
	
	public Set<Integer> getGeneratedIds() {
		return generatedIds;
	}

}

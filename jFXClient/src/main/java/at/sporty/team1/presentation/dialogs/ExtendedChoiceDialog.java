package at.sporty.team1.presentation.dialogs;

import java.util.Map;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;

public class ExtendedChoiceDialog<T extends Collection<U>, U> extends ChoiceDialog<U> {
	
	public ExtendedChoiceDialog(U defaultChoice, Collection<U> collection, Function<U, String> collectionConverter) {
		super(defaultChoice, convertCollection(collection));
	}
	
	private static <T extends Collection<U>, U> List<String> convertCollection(T collection) {
		Map<U, String> choiseMap = collection.stream().map(collectionConverter).collect(Collectors.toMap(keyMapper, valueMapper)); 
		
		
	}
}

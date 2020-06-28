package pxt.sgi.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 * Classe Objeto responsável pela recuperação do Path.
 * 
 * @author lauroof
 *
 * @param <D>
 * @param <T>
 */
public class SpecificationHelper<D, T> {

	/**
	 * Método que recupega o path recursivamente.
	 * 
	 * @param root
	 * @param key
	 * @return
	 */
	public Path<D> get(Root<T> root, String key) {
		String[] keys = null;
		// Path de retorno para atribuição dos atributos recursivos
		Path<D> path = null;
		// Path temporário para atribuição dos atributos recursivos
		Path<Object> pathAux = null;
		if (key.contains(".")) {
			keys = key.split("[.]");
			pathAux = root.get(keys[0]);
			for (int i = 1; i < keys.length; i++) {
				if (i == (keys.length - 1)) {
					path = pathAux.get(keys[i]);
				} else {
					pathAux = pathAux.get(keys[i]);
				}
			}
		} else {
			// Path único
			path = root.get(key);
		}
		return path;
	}

	/**
	 * 
	 * @param search
	 * @return List<SearchCriteria>
	 */
	public static List<SearchCriteria> stringForSearchCriteria(String search) {
		List<SearchCriteria> listaCriteria = new ArrayList<>();
		if (search != null && !search.isEmpty()) {
			String[] grupos = search.split(ConstantsSpecification.OB_DELIMITER);
			for (String grupo : grupos) {
				String[] fildValue = grupo.split(OperationsCriteria.REGEX_SPLIT_OP);
				listaCriteria.add(new SearchCriteria(fildValue[0].trim(), defineOperacao(grupo), fildValue[1]));
			}
		}
		return listaCriteria;
	}

	public static String defineOperacao(String grupo) {
		if (grupo.contains("+" + OperationsCriteria.EQ + "+")) {
			return OperationsCriteria.EQ;
		}
		if (grupo.contains("+" + OperationsCriteria.LK + "+")) {
			return OperationsCriteria.LK;
		}
		if (grupo.contains("+" + OperationsCriteria.GT + "+")) {
			return OperationsCriteria.GT;
		}

		if (grupo.contains("+" + OperationsCriteria.LT + "+")) {
			return OperationsCriteria.LT;
		}
		return null;
	}
}

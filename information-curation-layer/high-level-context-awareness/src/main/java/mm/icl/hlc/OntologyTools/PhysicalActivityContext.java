/**
* 
* Copyright [2016] [Claudia Villalonga & Muhammad Asif Razzaq]
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software distributed under 
* the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
*  ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and limitations under the License.
*/
package mm.icl.hlc.OntologyTools;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL2;
/**
 * PhysicalActivity Context is the class encapsulating the OntModel associated to a
 * PhysicalActivity Context Instance. This is an instance of the PhysicalActivity Context
 * class in the Context Ontology which represents the Mining Minds Context
 * Model. This class provides supporting methods to facilitate the access to
 * PhysicalActivity Context.
 * 
 * @author Claudia Villalonga and Muhammad Asif Razzaq
 * @version 2.5
 * @since 2015-10-28
 */
public class PhysicalActivityContext extends Context{   
	/**
	 * Constructor of a new PhysicalActivity Context instance.
	 * 
	 * @param hlcModel
	 *            OntModel associated to the PhysicalActivity Context instance.
	 */
	public PhysicalActivityContext(OntModel hlcModel) {
		super(hlcModel);
	}
	/**
	 * Constructor of a new PhysicalActivity Context instance.
	 * 
	 * @param hlcModel
	 *            OntModel associated to the PhysicalActivity Context instance.
	 * @param ont
	 *            Context Ontology which represents the Mining Minds Context
	 *            Model.
	 */
	public PhysicalActivityContext(OntModel hlcModel, ContextOntology ont) {
		super(hlcModel);
		OntModel hlcInst = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, hlcModel);
		hlcInst.addSubModel(ont.getCtxModel());
		ExtendedIterator<? extends OntResource> it = hlcInst.getOntClass(HLCA.pacClassName).listInstances(false); 
		if (it.hasNext()) {
			Individual inst = (Individual) it.next();
			if (!it.hasNext()) {
				super.ctxInstanceName = inst.getURI();
				ExtendedIterator<Resource> iter = inst.listRDFTypes(true);
				boolean classified = false;
				while (iter.hasNext()) {
					String uri = iter.next().getURI();
					if (uri != null && !uri.equals(OWL2.NamedIndividual.getURI())) {
						if (!uri.equals(HLCA.pacClassName)) {   
							super.ctxTypeName = uri;
							classified = true;
						}
					}
				}
				if (!classified)
					setUnidentifiedHlc();
			}
		}
	}
	/**
	 * Method to retrieve whether a PhysicalActivity Context instance is valid or not.
	 * 
	 * @return true if the PhysicalActivity Context Instance is valid, and false
	 *         otherwise.
	 */
	public boolean isValidInstanceOfHlc() {
		return (super.ctxInstanceName != null);
	}
	/**
	 * Method to retrieve whether a PhysicalActivity Context instance has been
	 * classified or not.
	 * 
	 * @return true if the PhysicalActivity Context Instance is classified, and false
	 *         otherwise.
	 */
	public boolean isClassified() {
		return (super.ctxTypeName != null && !super.ctxTypeName.equals(HLCA.unidentifiedHlc));
	}
	/**
	 * Method to set the PhysicalActivity Context Instance.
	 * 
	 * @param inst
	 *            OntResource representing the PhysicalActivity Context Instance.
	 */
	public void setHlcInstance(OntResource inst) {
		setCtxInstanceName(inst.getURI());
	}
	/**
	 * Method to set the class to which a the PhysicalActivity Context Instance
	 * belongs to.
	 * 
	 * @param c
	 *            OntClass to which the PhysicalActivity Context Instance belongs and
	 *            which has to be added.
	 */
	public void setHlcClass(OntClass c) {
		getCtxIndiv().addOntClass(c);
		setCtxTypeName(c.getURI());
	}
	/**
	 * Method to set that the PhysicalActivity Context Instance does not belongs to
	 * any of the defined classes in the Context Ontology. This process does not
	 * involve any changes in the OntModel associated to the PhysicalActivity Context
	 * Instance, but only setting the type of the Context Instance. This is done
	 * this way in order to avoid conflicts in the future and to ensure the
	 * Context Ontology extensibility.
	 * 
	 */
	public void setUnidentifiedHlc() {
		setCtxTypeName(HLCA.unidentifiedHlc);
	}
}
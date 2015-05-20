package org.intermine.bio.dataloader.job;


/**
 * Convenient entry point for building all kinds of steps. Use this as a factory for fluent builders of any step.
 *
 * @author Dave Syer
 *
 * @since 2.2
 */
public class StepBuilder extends StepBuilderHelper<StepBuilder> {

	/**
	 * Initialize a step builder for a step with the given name.
	 *
	 * @param name the name of the step
	 */
	public StepBuilder(String name) {
		super(name);
	}

}

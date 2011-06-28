/*
 * Copyright 2011 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.planner.core.bruteforce;

import java.util.Collection;
import java.util.Iterator;

import org.drools.FactHandle;
import org.drools.WorkingMemory;
import org.drools.planner.core.domain.meta.PlanningVariableDescriptor;
import org.drools.planner.core.solver.AbstractSolverPhaseScope;

public class BruteForcePlanningVariableIterator {

    private final AbstractSolverPhaseScope solverPhaseScope;
    private final Object planningEntity;
    private FactHandle planningEntityFactHandle;
    private final PlanningVariableDescriptor planningVariableDescriptor;

    private Collection<?> planningValues;
    private Iterator<?> planningValueIterator;

    private Object workingValue;

    public BruteForcePlanningVariableIterator(AbstractSolverPhaseScope solverPhaseScope,
            Object planningEntity, PlanningVariableDescriptor planningVariableDescriptor) {
        this.solverPhaseScope = solverPhaseScope;
        this.planningEntity = planningEntity;
        this.planningVariableDescriptor = planningVariableDescriptor;

        planningValues = planningVariableDescriptor.getRangeValues(solverPhaseScope.getWorkingSolution());
        planningValueIterator = planningValues.iterator();
    }

    public void setPlanningEntityFactHandle(FactHandle planningEntityFactHandle) {
        this.planningEntityFactHandle = planningEntityFactHandle;
    }

    public PlanningVariableDescriptor getPlanningVariableDescriptor() {
        return planningVariableDescriptor;
    }

    public Object getWorkingValue() {
        return workingValue;
    }

    public void initialize() {
        planningValueIterator = planningValues.iterator();
        Object value = planningValueIterator.next();
        planningVariableDescriptor.setValue(planningEntity, value);
        workingValue = value;
    }

    public void reset() {
        planningValueIterator = planningValues.iterator();
        Object value = planningValueIterator.next();
        changeWorkingValue(value);
        workingValue = value;
    }

    public boolean hasNext() {
        return planningValueIterator.hasNext();
    }

    public void next() {
        Object value = planningValueIterator.next();
        changeWorkingValue(value);
    }

    private void changeWorkingValue(Object value) {
        WorkingMemory workingMemory = solverPhaseScope.getWorkingMemory();
        planningVariableDescriptor.setValue(planningEntity, value);
        workingMemory.update(planningEntityFactHandle, planningEntity);
        workingValue = value;
    }

}

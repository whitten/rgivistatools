// Copyright 2012 Ray Group International
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.m.parsetree.filter;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.VistaPackage;

public class ExcludeNonPkgCallFanoutFilter extends SourcedFanoutFilter {
	private String sourcePackagePrefix;
	private RepositoryInfo repositoryInfo;
	
	public ExcludeNonPkgCallFanoutFilter(RepositoryInfo repositoryInfo) {
		this.repositoryInfo = repositoryInfo;
	}
	
	@Override
	public void setSource(EntryId source) {
		String routineName = source.getRoutineName();
		if (routineName == null) {
			this.sourcePackagePrefix = null;
		} else {
			VistaPackage vp = this.repositoryInfo.getPackageFromRoutineName(routineName);
			this.sourcePackagePrefix = vp.getDefaultPrefix();
		}
		super.setSource(source);
	}
	
	@Override
	public boolean isValid(EntryId input) {
		if (input != null) {
			String routineName = input.getRoutineName();
			if (routineName == null) return true;
			if (this.sourcePackagePrefix == null) return true;
			VistaPackage vp = this.repositoryInfo.getPackageFromRoutineName(routineName);
			String prefix = vp.getDefaultPrefix();
			return this.sourcePackagePrefix.equals(prefix);
		}
		return false;
	}
}
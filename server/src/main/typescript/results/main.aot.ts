import { platformBrowser } from '@angular/platform-browser';
import { decorateModuleRef } from './environment';

import { ResultsModuleNgFactory } from
  '../../../../target/ts_compiled/src/main/typescript/results/results.module.ngfactory';

export function main(): Promise<any> {
  return platformBrowser()
    .bootstrapModuleFactory(ResultsModuleNgFactory)
    .then(decorateModuleRef)
    .catch((err) => console.error(err));
}

export function bootstrapDomReady() {
  document.addEventListener('DOMContentLoaded', main);
}

bootstrapDomReady();

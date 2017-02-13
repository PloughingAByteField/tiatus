import { platformBrowser } from '@angular/platform-browser';
import { decorateModuleRef } from './environment';

import { AdjudicatorModuleNgFactory } from
  '../../../../compiled/src/main/typescript/adjudicator/adjudicator.module.ngfactory';

export function main(): Promise<any> {
  return platformBrowser()
    .bootstrapModuleFactory(AdjudicatorModuleNgFactory)
    .then(decorateModuleRef)
    .catch((err) => console.error(err));
}

export function bootstrapDomReady() {
  document.addEventListener('DOMContentLoaded', main);
}

bootstrapDomReady();

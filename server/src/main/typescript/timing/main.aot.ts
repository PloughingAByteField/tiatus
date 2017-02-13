import { platformBrowser } from '@angular/platform-browser';
import { decorateModuleRef } from './environment';

import { TimingModuleNgFactory } from
  '../../../../compiled/src/main/typescript/timing/timing.module.ngfactory';

export function main(): Promise<any> {
  return platformBrowser()
    .bootstrapModuleFactory(TimingModuleNgFactory)
    .then(decorateModuleRef)
    .catch((err) => console.error(err));
}

export function bootstrapDomReady() {
  document.addEventListener('DOMContentLoaded', main);
}

bootstrapDomReady();

import { platformBrowser } from '@angular/platform-browser';
import { decorateModuleRef } from './environment';

import { SetupModuleNgFactory } from
  '../../../../target/ts_compiled/src/main/typescript/setup/setup.module.ngfactory';

export function main(): Promise<any> {
  return platformBrowser()
    .bootstrapModuleFactory(SetupModuleNgFactory)
    .then(decorateModuleRef)
    .catch((err) => console.error(err));
}

export function bootstrapDomReady() {
  document.addEventListener('DOMContentLoaded', main);
}

bootstrapDomReady();

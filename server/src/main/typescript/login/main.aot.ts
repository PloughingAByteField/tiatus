import { platformBrowser } from '@angular/platform-browser';
import { decorateModuleRef } from './environment';

import { LoginModuleNgFactory } from
  '../../../../target/ts_compiled/src/main/typescript/login/login.module.ngfactory';

export function main(): Promise<any> {
  return platformBrowser()
    .bootstrapModuleFactory(LoginModuleNgFactory)
    .then(decorateModuleRef)
    .catch((err) => console.error(err));
}

export function bootstrapDomReady() {
  document.addEventListener('DOMContentLoaded', main);
}

bootstrapDomReady();

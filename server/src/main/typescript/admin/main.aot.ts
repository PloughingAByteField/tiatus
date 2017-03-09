import { platformBrowser } from '@angular/platform-browser';
import { decorateModuleRef } from './environment';

import { AdminModuleNgFactory } from
  '../../../../target/ts_compiled/src/main/typescript/admin/admin.module.ngfactory';

export function main(): Promise<any> {
  return platformBrowser()
    .bootstrapModuleFactory(AdminModuleNgFactory)
    .then(decorateModuleRef)
    .catch((err) => console.error(err));
}

export function bootstrapDomReady() {
  document.addEventListener('DOMContentLoaded', main);
}

bootstrapDomReady();

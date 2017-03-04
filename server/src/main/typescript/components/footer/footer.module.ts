import { NgModule } from '@angular/core';

import { FooterComponent } from './footer.component';

import { ConfigService } from '../../config/config.service';
import { ConfigHttpService } from '../../config/config-http.service';

@NgModule({
    declarations: [
        FooterComponent
    ],
    exports: [ FooterComponent ],
    providers: [ ConfigService, ConfigHttpService ]
})
export class FooterModule { }

import { NgModule } from '@angular/core';

import { ConfigService } from '../../services/config.service';

import { TitlebarComponent } from './titlebar.component';

@NgModule({
    declarations: [
        TitlebarComponent
    ],
    exports: [ TitlebarComponent ],
    providers: [ ConfigService ]
})
export class TitlebarModule { }

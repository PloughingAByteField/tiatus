import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ConfigService } from '../../config/config.service';
import { ConfigHttpService } from '../../config/config-http.service';

import { TitlebarComponent } from './titlebar.component';

@NgModule({
    declarations: [
        TitlebarComponent
    ],
    imports: [
        CommonModule
    ],
    exports: [ TitlebarComponent ],
    providers: [ ConfigService, ConfigHttpService ]
})
export class TitlebarModule { }

import { NgModule } from '@angular/core';
import { TitlebarComponent } from './titlebar.component';

@NgModule({
    declarations: [
        TitlebarComponent
    ],
    exports: [ TitlebarComponent ]
})
export class TitlebarModule { }

import { NgModule } from '@angular/core';
import { NoContentComponent } from './no-content.component';

@NgModule({
    declarations: [
        NoContentComponent
    ],
    exports: [ NoContentComponent ]
})
export class NoContentModule { }

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { TimeEntryComponent } from './time-entry.component';

@NgModule({
    declarations: [
        TimeEntryComponent
    ],
    imports: [
        CommonModule,
        FormsModule
    ],
    exports: [ TimeEntryComponent ]
})
export class TimeEntryModule { }

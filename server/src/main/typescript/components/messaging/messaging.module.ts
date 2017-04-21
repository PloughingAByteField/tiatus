import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TranslateModule } from '@ngx-translate/core';

import { MessagingComponent } from './messaging.component';

@NgModule({
    declarations: [
        MessagingComponent
    ],
    imports: [
        CommonModule,
        TranslateModule.forRoot()
    ],
    exports: [ MessagingComponent ]
})
export class MessagingModule { }

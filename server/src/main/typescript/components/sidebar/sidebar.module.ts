import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from './sidebar.component';
import { RouterModule } from '@angular/router';
import { TranslateModule } from 'ng2-translate';

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        TranslateModule.forRoot()
    ],
    declarations: [
        SidebarComponent
    ],
    exports: [ SidebarComponent ]
})
export class SidebarModule { }

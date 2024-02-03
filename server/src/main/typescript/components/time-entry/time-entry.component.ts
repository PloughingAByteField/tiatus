import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'time-entry',
  styleUrls: [ './time-entry.component.css' ],
  templateUrl: './time-entry.component.html',
})
export class TimeEntryComponent implements OnInit {
  @Input()
  public time: number;
  @Input()
  public displayMillSeconds: boolean;
  @Output()
  public updatedTime: EventEmitter<number> = new EventEmitter<number>();

  public hours: string = null;
  public minutes: string = null;
  public seconds: string = null;
  public milliSeconds: string = null;

  public ngOnInit() {
    if (this.time !== 0) {
      this.fillFieldsForTime(this.time);
    }
  }

  public onBlur(event: any): void {
    let lostFocus: boolean = true;
    if (event.composedPath() !== undefined && event.composedPath().length >= 1) {
      const currentId: number = event.composedPath()[1].id;
      if (event.relatedTarget != null && event.relatedTarget.parentNode != null) {
        const targetId: number = event.relatedTarget.parentNode.id;
        if (currentId === targetId) {
          lostFocus = false;
        }
      }
    }
    if (lostFocus) {
      let resetFields: boolean = false;
      if (isNaN(+this.hours)) {
        resetFields = true;
      }
      if (isNaN(+this.minutes)) {
        resetFields = true;
      }
      if (isNaN(+this.seconds)) {
        resetFields = true;
      }
      if (isNaN(+this.milliSeconds)) {
        resetFields = true;
      }
      if (resetFields) {
        this.fillFieldsForTime(this.time);
      } else {
        if (this.time != 0) {
          this.updateTime();
        }
      }
    }
  }

  private updateTime(): void {
    let date: Date;
    if (this.time === 0) {
      date = new Date(0);
    } else {
      date = new Date(this.time);
    }
    if (this.hours) {
      date.setUTCHours(+this.hours);
    }
    if (this.minutes) {
      date.setUTCMinutes(+this.minutes);
    }
    if (this.seconds) {
      date.setUTCSeconds(+this.seconds);
    }
    if (this.milliSeconds) {
      date.setUTCMilliseconds(+this.milliSeconds);
    }
    this.time = date.getTime();
    this.fillFieldsForTime(this.time);
    this.fireUpdatedTime();
  }

  private fillFieldsForTime(time: number): void {
    if (time !== 0) {
      const date = new Date(this.time);
      this.hours = ('0' + date.getUTCHours()).slice(-2);
      this.minutes = ('0' + date.getUTCMinutes()).slice(-2);
      this.seconds = ('0' + date.getUTCSeconds()).slice(-2);
      this.milliSeconds = ('000' + date.getUTCMilliseconds()).slice(-3);
    } else {
      this.hours = null;
      this.minutes = null;
      this.seconds = null;
      this.milliSeconds = null;
    }
  }

  private fireUpdatedTime(): void {
    this.updatedTime.next(this.time);
  }
}

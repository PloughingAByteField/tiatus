import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormArray } from '@angular/forms';
import { Validators, AbstractControl, ValidatorFn } from '@angular/forms';

import { Position } from '../../positions/position.model';
import { AdminPositionsService } from './positions.service';

@Component({
  selector: 'positions',
  styleUrls: [ './positions.component.css' ],
  templateUrl: './positions.component.html'
})
export class PositionsComponent implements OnInit {

  public addPositionForm: FormGroup;
  public positionsForm: FormGroup;

  public positions: Position[] = new Array<Position>();

  constructor(private positionsService: AdminPositionsService) {}

  public ngOnInit() {
    this.addPositionForm = new FormGroup({
      name: this.getNameControlWithValidators('')
    });
    this.positionsService.getPositions()
      .subscribe((positions: Position[]) => {
        this.positions = positions;
        this.positionsForm = new FormGroup({
          positions: new FormArray([])
        });
        this.positions.map((position: Position) => this.addPositionToFormArray(position));
      });
  }

  public addPositionToFormArray(position: Position): void {
    const nameControl: FormControl = this.getNameControlWithValidators(position.name);
    const positionControl: FormControl = new FormControl(position);
    const positionGroup = new FormGroup({
      name: nameControl,
      position: positionControl
    });
    const array: FormArray = this.getPositionFormArray();
    array.push(positionGroup);
  }

  public removePosition(position: Position): void {
    console.log(position);
    if (position) {
      this.positionsService.removePosition(position);
    }
  }

  public updatePosition(data: string, position: Position): void {
    console.log(data);
    console.log(position);
    position.name = data;
    if (position) {
      position.name = data;
      this.positionsService.updatePosition(position);
    }
  }

  public onSubmit({ value, valid }: { value: Position, valid: boolean }) {
    const position: Position = new Position();
    position.name = value.name;
    this.positionsService.addPosition(position);
    this.addPositionForm.reset({
      name: ''
    });
  }

  private getPositionFormArray(): FormArray {
    if (this.positionsForm) {
      return this.positionsForm.get('positions') as FormArray;
    }
  }

  private getNameControlWithValidators(value: string): FormControl {
    return new FormControl(value, [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(250),
        this.validatePositionName()
      ]);
  }

  private validatePositionName(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      for (const position of this.positions) {
        if (position.name === control.value) {
          return { existingName: true };
        }
      }

      return null;
    };
  }
}

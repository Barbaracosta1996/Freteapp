import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISettingsCargaApp, NewSettingsCargaApp } from '../settings-carga-app.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISettingsCargaApp for edit and NewSettingsCargaAppFormGroupInput for create.
 */
type SettingsCargaAppFormGroupInput = ISettingsCargaApp | PartialWithRequiredKeyOf<NewSettingsCargaApp>;

type SettingsCargaAppFormDefaults = Pick<NewSettingsCargaApp, 'id'>;

type SettingsCargaAppFormGroupContent = {
  id: FormControl<ISettingsCargaApp['id'] | NewSettingsCargaApp['id']>;
  rdCode: FormControl<ISettingsCargaApp['rdCode']>;
  waToken: FormControl<ISettingsCargaApp['waToken']>;
};

export type SettingsCargaAppFormGroup = FormGroup<SettingsCargaAppFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SettingsCargaAppFormService {
  createSettingsCargaAppFormGroup(settingsCargaApp: SettingsCargaAppFormGroupInput = { id: null }): SettingsCargaAppFormGroup {
    const settingsCargaAppRawValue = {
      ...this.getFormDefaults(),
      ...settingsCargaApp,
    };
    return new FormGroup<SettingsCargaAppFormGroupContent>({
      id: new FormControl(
        { value: settingsCargaAppRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      rdCode: new FormControl(settingsCargaAppRawValue.rdCode),
      waToken: new FormControl(settingsCargaAppRawValue.waToken),
    });
  }

  getSettingsCargaApp(form: SettingsCargaAppFormGroup): ISettingsCargaApp | NewSettingsCargaApp {
    return form.getRawValue() as ISettingsCargaApp | NewSettingsCargaApp;
  }

  resetForm(form: SettingsCargaAppFormGroup, settingsCargaApp: SettingsCargaAppFormGroupInput): void {
    const settingsCargaAppRawValue = { ...this.getFormDefaults(), ...settingsCargaApp };
    form.reset(
      {
        ...settingsCargaAppRawValue,
        id: { value: settingsCargaAppRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SettingsCargaAppFormDefaults {
    return {
      id: null,
    };
  }
}

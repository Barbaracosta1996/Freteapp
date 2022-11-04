import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISettingsContracts, NewSettingsContracts } from '../settings-contracts.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISettingsContracts for edit and NewSettingsContractsFormGroupInput for create.
 */
type SettingsContractsFormGroupInput = ISettingsContracts | PartialWithRequiredKeyOf<NewSettingsContracts>;

type SettingsContractsFormDefaults = Pick<NewSettingsContracts, 'id'>;

type SettingsContractsFormGroupContent = {
  id: FormControl<ISettingsContracts['id'] | NewSettingsContracts['id']>;
  terms: FormControl<ISettingsContracts['terms']>;
  termsContentType: FormControl<ISettingsContracts['termsContentType']>;
  contractDefault: FormControl<ISettingsContracts['contractDefault']>;
  contractDefaultContentType: FormControl<ISettingsContracts['contractDefaultContentType']>;
  privacy: FormControl<ISettingsContracts['privacy']>;
  privacyContentType: FormControl<ISettingsContracts['privacyContentType']>;
};

export type SettingsContractsFormGroup = FormGroup<SettingsContractsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SettingsContractsFormService {
  createSettingsContractsFormGroup(settingsContracts: SettingsContractsFormGroupInput = { id: null }): SettingsContractsFormGroup {
    const settingsContractsRawValue = {
      ...this.getFormDefaults(),
      ...settingsContracts,
    };
    return new FormGroup<SettingsContractsFormGroupContent>({
      id: new FormControl(
        { value: settingsContractsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      terms: new FormControl(settingsContractsRawValue.terms),
      termsContentType: new FormControl(settingsContractsRawValue.termsContentType),
      contractDefault: new FormControl(settingsContractsRawValue.contractDefault),
      contractDefaultContentType: new FormControl(settingsContractsRawValue.contractDefaultContentType),
      privacy: new FormControl(settingsContractsRawValue.privacy),
      privacyContentType: new FormControl(settingsContractsRawValue.privacyContentType),
    });
  }

  getSettingsContracts(form: SettingsContractsFormGroup): ISettingsContracts | NewSettingsContracts {
    return form.getRawValue() as ISettingsContracts | NewSettingsContracts;
  }

  resetForm(form: SettingsContractsFormGroup, settingsContracts: SettingsContractsFormGroupInput): void {
    const settingsContractsRawValue = { ...this.getFormDefaults(), ...settingsContracts };
    form.reset(
      {
        ...settingsContractsRawValue,
        id: { value: settingsContractsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SettingsContractsFormDefaults {
    return {
      id: null,
    };
  }
}

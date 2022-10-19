import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../categoria-veiculo.test-samples';

import { CategoriaVeiculoFormService } from './categoria-veiculo-form.service';

describe('CategoriaVeiculo Form Service', () => {
  let service: CategoriaVeiculoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CategoriaVeiculoFormService);
  });

  describe('Service methods', () => {
    describe('createCategoriaVeiculoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCategoriaVeiculoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
          })
        );
      });

      it('passing ICategoriaVeiculo should create a new form with FormGroup', () => {
        const formGroup = service.createCategoriaVeiculoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
          })
        );
      });
    });

    describe('getCategoriaVeiculo', () => {
      it('should return NewCategoriaVeiculo for default CategoriaVeiculo initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCategoriaVeiculoFormGroup(sampleWithNewData);

        const categoriaVeiculo = service.getCategoriaVeiculo(formGroup) as any;

        expect(categoriaVeiculo).toMatchObject(sampleWithNewData);
      });

      it('should return NewCategoriaVeiculo for empty CategoriaVeiculo initial value', () => {
        const formGroup = service.createCategoriaVeiculoFormGroup();

        const categoriaVeiculo = service.getCategoriaVeiculo(formGroup) as any;

        expect(categoriaVeiculo).toMatchObject({});
      });

      it('should return ICategoriaVeiculo', () => {
        const formGroup = service.createCategoriaVeiculoFormGroup(sampleWithRequiredData);

        const categoriaVeiculo = service.getCategoriaVeiculo(formGroup) as any;

        expect(categoriaVeiculo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICategoriaVeiculo should not enable id FormControl', () => {
        const formGroup = service.createCategoriaVeiculoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCategoriaVeiculo should disable id FormControl', () => {
        const formGroup = service.createCategoriaVeiculoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

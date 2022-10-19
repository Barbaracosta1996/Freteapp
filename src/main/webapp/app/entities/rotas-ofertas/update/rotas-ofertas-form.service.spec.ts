import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../rotas-ofertas.test-samples';

import { RotasOfertasFormService } from './rotas-ofertas-form.service';

describe('RotasOfertas Form Service', () => {
  let service: RotasOfertasFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RotasOfertasFormService);
  });

  describe('Service methods', () => {
    describe('createRotasOfertasFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRotasOfertasFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rotas: expect.any(Object),
            ofertas: expect.any(Object),
          })
        );
      });

      it('passing IRotasOfertas should create a new form with FormGroup', () => {
        const formGroup = service.createRotasOfertasFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rotas: expect.any(Object),
            ofertas: expect.any(Object),
          })
        );
      });
    });

    describe('getRotasOfertas', () => {
      it('should return NewRotasOfertas for default RotasOfertas initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRotasOfertasFormGroup(sampleWithNewData);

        const rotasOfertas = service.getRotasOfertas(formGroup) as any;

        expect(rotasOfertas).toMatchObject(sampleWithNewData);
      });

      it('should return NewRotasOfertas for empty RotasOfertas initial value', () => {
        const formGroup = service.createRotasOfertasFormGroup();

        const rotasOfertas = service.getRotasOfertas(formGroup) as any;

        expect(rotasOfertas).toMatchObject({});
      });

      it('should return IRotasOfertas', () => {
        const formGroup = service.createRotasOfertasFormGroup(sampleWithRequiredData);

        const rotasOfertas = service.getRotasOfertas(formGroup) as any;

        expect(rotasOfertas).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRotasOfertas should not enable id FormControl', () => {
        const formGroup = service.createRotasOfertasFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRotasOfertas should disable id FormControl', () => {
        const formGroup = service.createRotasOfertasFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

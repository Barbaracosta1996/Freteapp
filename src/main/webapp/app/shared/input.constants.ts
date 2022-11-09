export const DATE_FORMAT = 'YYYY-MM-DD';
export const DATE_TIME_FORMAT = 'YYYY-MM-DDTHH:mm';
export const ISO_MOMENT_FORMAT = 'YYYY-MM-DDTHH:mm:ss';
export const BR_DATE_FORMAT = 'DD/MM/YYYY';
export const BR_DATE_TIME_FORMAT = 'DD/MM/YYYYTHH:mm';
export const CELLPHONE_MASK = ['(', /[1-9]/, /\d/, ')', ' ', /\d/, /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/];
export const LANDLINE_MASK = ['(', /[1-9]/, /\d/, ')', ' ', /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/];
export const CNPJ_MASK = [/\d/, /\d/, '.', /\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '/', /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/];
export const CPF_MASK = [/\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '-', /\d/, /\d/];
export const POSTAL_MASK = [/\d/, /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/];
export const PHONE_MASK = function (rawValue: any) {
  const numbers = rawValue.match(/\d/g);
  let numberLength = 0;
  if (numbers) {
    numberLength = numbers.join('').length;
  }
  if (numberLength <= 10) {
    return LANDLINE_MASK;
  } else {
    return CELLPHONE_MASK;
  }
};

export const CNPJ_OR_CPF_MASK = function (rawValue: any) {
  const numbers = rawValue.match(/\d/g);
  let numberLength = 0;
  if (numbers) {
    numberLength = numbers.join('').length;
  }
  if (numberLength <= 11) {
    return CPF_MASK;
  } else {
    return CNPJ_MASK;
  }
};

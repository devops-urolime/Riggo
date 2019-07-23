import * as mdc from 'material-components-web';

// Material init.
export const selectToInitMDCRipple = (selectorToUse) => {
  const elementsMDCRippleToInit = document.querySelectorAll(selectorToUse);
  if (elementsMDCRippleToInit) {
    [].map.call(elementsMDCRippleToInit, el => mdc.ripple.MDCRipple.attachTo(el));
  }
};

export const selectToInitMDCFloatingLabel = (selectorToUse) => {
  const elementsMDCRippleToInit = document.querySelectorAll(selectorToUse);
  if (elementsMDCRippleToInit) {
    [].map.call(elementsMDCRippleToInit, el => mdc.floatingLabel.MDCFloatingLabel.attachTo(el));
  }
};


export const selectToInitMDCTextField = (selectorToUse) => {
  const elementsMDCRippleToInit = document.querySelectorAll(selectorToUse);
  if (elementsMDCRippleToInit) {
    [].map.call(elementsMDCRippleToInit, el => mdc.textField.MDCTextField.attachTo(el));
  }
};

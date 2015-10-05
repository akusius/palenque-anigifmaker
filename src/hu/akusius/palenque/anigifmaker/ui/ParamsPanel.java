package hu.akusius.palenque.anigifmaker.ui;

import hu.akusius.palenque.anigifmaker.AnimParams;
import hu.akusius.palenque.anigifmaker.AnimParamsBuilder;
import hu.akusius.palenque.anigifmaker.ParamRange;
import hu.akusius.palenque.anigifmaker.util.UIUtils;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.other.ChangeSupport;

/**
 *
 * @author Bujdosó Ákos
 */
public class ParamsPanel extends javax.swing.JPanel {

  /** Creates new form ParamsPanel */
  @SuppressWarnings("LeakingThisInConstructor")
  public ParamsPanel() {
    initComponents();
    gridPanel.setFocusTraversalPolicyProvider(true);
    gridPanel.setFocusTraversalPolicy(new VerticalLayoutFocusTraversalPolicy());

    setSpinnerModelFromParamRange(spWidth, AnimParams.WH_RANGE, 50);
    setSpinnerModelFromParamRange(spHeight, AnimParams.WH_RANGE, 50);
    setSpinnerModelFromParamRange(spFirstStep, AnimParams.STEP_RANGE, 1);
    setSpinnerModelFromParamRange(spLastStep, AnimParams.STEP_RANGE, 1);
    setSpinnerModelFromParamRange(spSpeed, AnimParams.SPEED_RANGE, 1);
    setSpinnerModelFromParamRange(spFPS, AnimParams.FPS_RANGE, 1);
    setSpinnerModelFromParamRange(spTranslateX, AnimParams.TRANSLATE_RANGE, 1.0);
    setSpinnerModelFromParamRange(spTranslateY, AnimParams.TRANSLATE_RANGE, 1.0);
    setSpinnerModelFromParamRange(spTheta, AnimParams.THETA_PHI_RANGE, .2);
    setSpinnerModelFromParamRange(spPhi, AnimParams.THETA_PHI_RANGE, .2);
    setSpinnerModelFromParamRange(spZoom, AnimParams.ZOOM_RANGE, .2);
    setSpinnerModelFromParamRange(spHoldIn, AnimParams.HOLDINOUT_RANGE, 5);
    setSpinnerModelFromParamRange(spHoldOut, AnimParams.HOLDINOUT_RANGE, 5);
    setSpinnerModelFromParamRange(spFadeIn, AnimParams.FADEINOUT_RANGE, 5);
    setSpinnerModelFromParamRange(spFadeOut, AnimParams.FADEINOUT_RANGE, 5);

    // Az alapértelmezett paraméterek beállítása
    fromParams(new AnimParamsBuilder().createAnimParams());

    // Változások jelzéséhez eseménykezelők rákötése
    new Object() {
      void recursive(JComponent parent) {
        synchronized (parent.getTreeLock()) {
          Component[] components = parent.getComponents();
          for (Component component : components) {
            if (component instanceof JSpinner) {
              JSpinner sp = (JSpinner) component;
              sp.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                  fireChange();
                }
              });
            } else if (component instanceof JCheckBox) {
              JCheckBox chk = (JCheckBox) component;
              chk.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                  fireChange();
                }
              });
            } else if (component instanceof JTextField) {
              JTextField tf = (JTextField) component;
              tf.getDocument().addDocumentListener(new DocumentListener() {
                private void changed() {
                  fireChange();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                  changed();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                  changed();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                  changed();
                }
              });
            } else if (component instanceof JPanel) {
              recursive((JComponent) component);
            }
          }
        }
      }
    }.recursive(this);
  }

  private static <T extends Number & Comparable<T>> void setSpinnerModelFromParamRange(JSpinner spinner, ParamRange<T> range, T step) {
    SpinnerNumberModel model = new SpinnerNumberModel(range.getDef(), range.getMin(), range.getMax(), step);
    spinner.setModel(model);
  }

  /**
   * Az értékek beállítása paraméterekből.
   * @param params Az értékek forrása.
   */
  public final void fromParams(AnimParams params) {
    changeNotificationDeferred = true;

    try {
      spWidth.setValue(params.getWidth());
      spHeight.setValue(params.getHeight());
      spFirstStep.setValue(params.getFirstStep());
      spLastStep.setValue(params.getLastStep());
      spSpeed.setValue(params.getSpeed());
      spFPS.setValue(params.getFramesPerSecond());
      chkShowGrid.setSelected(params.isShowGrid());
      chkAutoCam.setSelected(params.isAutoCam());
      spTranslateX.setValue(params.getTranslateX());
      spTranslateY.setValue(params.getTranslateY());
      spTheta.setValue(params.getTheta());
      spPhi.setValue(params.getPhi());
      spZoom.setValue(params.getZoom());
      spHoldIn.setValue(params.getHoldInLength());
      spHoldOut.setValue(params.getHoldOutLength());
      spFadeIn.setValue(params.getFadeInLength());
      spFadeOut.setValue(params.getFadeOutLength());
      tfDesc.setText(params.getDesc());
    } finally {
      changeNotificationDeferred = false;
      fireChange();
    }
  }

  /**
   * A paraméterek összeállítása az aktuális értékekből.
   * @return Az összeállított paraméterek.
   * @throws IllegalStateException Nem sikerült az összeállítás, hibás értékek vannak megadva.
   */
  public AnimParams toParams() throws IllegalStateException {
    AnimParamsBuilder apb = new AnimParamsBuilder();
    apb.setWidth((int) spWidth.getValue());
    apb.setHeight((int) spHeight.getValue());
    apb.setFirstStep((int) spFirstStep.getValue());
    apb.setLastStep((int) spLastStep.getValue());
    apb.setSpeed((int) spSpeed.getValue());
    apb.setFramesPerSecond((int) spFPS.getValue());
    apb.setShowGrid(chkShowGrid.isSelected());
    apb.setAutoCam(chkAutoCam.isSelected());
    apb.setTranslateX((double) spTranslateX.getValue());
    apb.setTranslateY((double) spTranslateY.getValue());
    apb.setTheta((double) spTheta.getValue());
    apb.setPhi((double) spPhi.getValue());
    apb.setZoom((double) spZoom.getValue());
    apb.setHoldInLength((int) spHoldIn.getValue());
    apb.setHoldOutLength((int) spHoldOut.getValue());
    apb.setFadeInLength((int) spFadeIn.getValue());
    apb.setFadeOutLength((int) spFadeOut.getValue());
    apb.setDesc(tfDesc.getText());

    try {
      return apb.createAnimParams();
    } catch (IllegalArgumentException ex) {
      throw new IllegalStateException(ex);
    }
  }

  private final ChangeSupport changeSupport = new ChangeSupport(this);

  /**
   *
   * @param listener
   */
  public void addChangeListener(ChangeListener listener) {
    changeSupport.addChangeListener(listener);
  }

  /**
   *
   * @param listener
   */
  public void removeChangeListener(ChangeListener listener) {
    changeSupport.addChangeListener(listener);
  }

  private boolean changeNotificationDeferred = false;

  private void fireChange() {
    if (!changeNotificationDeferred) {
      changeSupport.fireChange();
    }
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jPanel2 = new javax.swing.JPanel();
    gridPanel = new javax.swing.JPanel();
    spHeight = new javax.swing.JSpinner();
    spWidth = new javax.swing.JSpinner();
    spFirstStep = new javax.swing.JSpinner();
    spLastStep = new javax.swing.JSpinner();
    jLabel2 = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jLabel12 = new javax.swing.JLabel();
    spHoldIn = new javax.swing.JSpinner();
    jLabel13 = new javax.swing.JLabel();
    spHoldOut = new javax.swing.JSpinner();
    jLabel14 = new javax.swing.JLabel();
    spFadeIn = new javax.swing.JSpinner();
    jLabel15 = new javax.swing.JLabel();
    spFadeOut = new javax.swing.JSpinner();
    jLabel5 = new javax.swing.JLabel();
    spSpeed = new javax.swing.JSpinner();
    jLabel6 = new javax.swing.JLabel();
    spFPS = new javax.swing.JSpinner();
    jLabel7 = new javax.swing.JLabel();
    spTranslateX = new javax.swing.JSpinner();
    jLabel8 = new javax.swing.JLabel();
    spTranslateY = new javax.swing.JSpinner();
    jLabel10 = new javax.swing.JLabel();
    spPhi = new javax.swing.JSpinner();
    spTheta = new javax.swing.JSpinner();
    spZoom = new javax.swing.JSpinner();
    jLabel9 = new javax.swing.JLabel();
    jLabel11 = new javax.swing.JLabel();
    chkShowGrid = new javax.swing.JCheckBox();
    filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
    filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
    filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
    jPanel3 = new javax.swing.JPanel();
    filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
    filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
    chkAutoCam = new javax.swing.JCheckBox();
    jPanel4 = new javax.swing.JPanel();
    jLabel16 = new javax.swing.JLabel();
    tfDesc = new javax.swing.JTextField();
    btnExport = new javax.swing.JButton();
    btnReset = new javax.swing.JButton();
    btnImport = new javax.swing.JButton();

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 100, Short.MAX_VALUE)
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 100, Short.MAX_VALUE)
    );

    setLayout(new java.awt.BorderLayout());

    gridPanel.setLayout(new java.awt.GridBagLayout());

    spHeight.setFont(spHeight.getFont().deriveFont(spHeight.getFont().getSize()-1f));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridPanel.add(spHeight, gridBagConstraints);

    spWidth.setFont(spWidth.getFont().deriveFont(spWidth.getFont().getSize()-1f));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.weightx = 1.0;
    gridPanel.add(spWidth, gridBagConstraints);

    spFirstStep.setFont(spFirstStep.getFont().deriveFont(spFirstStep.getFont().getSize()-1f));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridPanel.add(spFirstStep, gridBagConstraints);

    spLastStep.setFont(spLastStep.getFont().deriveFont(spLastStep.getFont().getSize()-1f));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridPanel.add(spLastStep, gridBagConstraints);

    jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getSize()-1f));
    jLabel2.setText("Height:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridPanel.add(jLabel2, gridBagConstraints);

    jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getSize()-1f));
    jLabel1.setText("Width:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridPanel.add(jLabel1, gridBagConstraints);

    jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getSize()-1f));
    jLabel3.setText("First step:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridPanel.add(jLabel3, gridBagConstraints);

    jLabel4.setFont(jLabel4.getFont().deriveFont(jLabel4.getFont().getSize()-1f));
    jLabel4.setText("Last step:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
    gridPanel.add(jLabel4, gridBagConstraints);

    jLabel12.setFont(jLabel12.getFont().deriveFont(jLabel12.getFont().getSize()-1f));
    jLabel12.setText("Hold in:");
    jLabel12.setToolTipText("Hold length at the start");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridPanel.add(jLabel12, gridBagConstraints);

    spHoldIn.setFont(spHoldIn.getFont().deriveFont(spHoldIn.getFont().getSize()-1f));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 5;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridPanel.add(spHoldIn, gridBagConstraints);

    jLabel13.setFont(jLabel13.getFont().deriveFont(jLabel13.getFont().getSize()-1f));
    jLabel13.setText("Hold out:");
    jLabel13.setToolTipText("Hold length at the end");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridPanel.add(jLabel13, gridBagConstraints);

    spHoldOut.setFont(spHoldOut.getFont().deriveFont(spHoldOut.getFont().getSize()-1f));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 5;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridPanel.add(spHoldOut, gridBagConstraints);

    jLabel14.setFont(jLabel14.getFont().deriveFont(jLabel14.getFont().getSize()-1f));
    jLabel14.setText("Fade in:");
    jLabel14.setToolTipText("Fade length at the start");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridPanel.add(jLabel14, gridBagConstraints);

    spFadeIn.setFont(spFadeIn.getFont().deriveFont(spFadeIn.getFont().getSize()-1f));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 5;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridPanel.add(spFadeIn, gridBagConstraints);

    jLabel15.setFont(jLabel15.getFont().deriveFont(jLabel15.getFont().getSize()-1f));
    jLabel15.setText("Fade out:");
    jLabel15.setToolTipText("Fade length at the end");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
    gridPanel.add(jLabel15, gridBagConstraints);

    spFadeOut.setFont(spFadeOut.getFont().deriveFont(spFadeOut.getFont().getSize()-1f));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 5;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridPanel.add(spFadeOut, gridBagConstraints);

    jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getSize()-1f));
    jLabel5.setText("Speed:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
    gridPanel.add(jLabel5, gridBagConstraints);

    spSpeed.setFont(spSpeed.getFont().deriveFont(spSpeed.getFont().getSize()-1f));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 8;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridPanel.add(spSpeed, gridBagConstraints);

    jLabel6.setFont(jLabel6.getFont().deriveFont(jLabel6.getFont().getSize()-1f));
    jLabel6.setText("FPS:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridPanel.add(jLabel6, gridBagConstraints);

    spFPS.setFont(spFPS.getFont().deriveFont(spFPS.getFont().getSize()-1f));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 8;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridPanel.add(spFPS, gridBagConstraints);

    jLabel7.setFont(jLabel7.getFont().deriveFont(jLabel7.getFont().getSize()-1f));
    jLabel7.setText("Translate X:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridPanel.add(jLabel7, gridBagConstraints);

    spTranslateX.setFont(spTranslateX.getFont().deriveFont(spTranslateX.getFont().getSize()-1f));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 8;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridPanel.add(spTranslateX, gridBagConstraints);

    jLabel8.setFont(jLabel8.getFont().deriveFont(jLabel8.getFont().getSize()-1f));
    jLabel8.setText("Translate Y:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridPanel.add(jLabel8, gridBagConstraints);

    spTranslateY.setFont(spTranslateY.getFont().deriveFont(spTranslateY.getFont().getSize()-1f));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 8;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridPanel.add(spTranslateY, gridBagConstraints);

    jLabel10.setFont(jLabel10.getFont().deriveFont(jLabel10.getFont().getSize()-1f));
    jLabel10.setText("Phi:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 10;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridPanel.add(jLabel10, gridBagConstraints);

    spPhi.setFont(spPhi.getFont().deriveFont(spPhi.getFont().getSize()-1f));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 11;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridPanel.add(spPhi, gridBagConstraints);

    spTheta.setFont(spTheta.getFont().deriveFont(spTheta.getFont().getSize()-1f));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 11;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridPanel.add(spTheta, gridBagConstraints);

    spZoom.setFont(spZoom.getFont().deriveFont(spZoom.getFont().getSize()-1f));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 11;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridPanel.add(spZoom, gridBagConstraints);

    jLabel9.setFont(jLabel9.getFont().deriveFont(jLabel9.getFont().getSize()-1f));
    jLabel9.setText("Theta:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 10;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridPanel.add(jLabel9, gridBagConstraints);

    jLabel11.setFont(jLabel11.getFont().deriveFont(jLabel11.getFont().getSize()-1f));
    jLabel11.setText("Zooming:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 10;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
    gridPanel.add(jLabel11, gridBagConstraints);

    chkShowGrid.setFont(chkShowGrid.getFont().deriveFont(chkShowGrid.getFont().getSize()-1f));
    chkShowGrid.setText("Grid");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 11;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridPanel.add(chkShowGrid, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.ipadx = 10;
    gridPanel.add(filler1, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.ipadx = 10;
    gridPanel.add(filler2, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 9;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.ipadx = 10;
    gridPanel.add(filler3, gridBagConstraints);

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 10;
    gridBagConstraints.gridy = 0;
    gridPanel.add(jPanel3, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.ipadx = 5;
    gridPanel.add(filler4, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 12;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.ipadx = 5;
    gridPanel.add(filler5, gridBagConstraints);

    chkAutoCam.setFont(chkAutoCam.getFont().deriveFont(chkAutoCam.getFont().getSize()-1f));
    chkAutoCam.setText("Auto");
    chkAutoCam.setToolTipText("Automatic camera movement");
    chkAutoCam.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        chkAutoCamItemStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 10;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridPanel.add(chkAutoCam, gridBagConstraints);

    add(gridPanel, java.awt.BorderLayout.CENTER);

    jLabel16.setFont(jLabel16.getFont().deriveFont(jLabel16.getFont().getSize()-1f));
    jLabel16.setText("Description:");

    tfDesc.setFont(tfDesc.getFont().deriveFont(tfDesc.getFont().getSize()-1f));

    btnExport.setFont(btnExport.getFont().deriveFont(btnExport.getFont().getSize()-1f));
    btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hu/akusius/palenque/anigifmaker/ui/icons/export.png"))); // NOI18N
    btnExport.setToolTipText("Export");
    btnExport.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnExportActionPerformed(evt);
      }
    });

    btnReset.setFont(btnReset.getFont().deriveFont(btnReset.getFont().getSize()-1f));
    btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hu/akusius/palenque/anigifmaker/ui/icons/reset.png"))); // NOI18N
    btnReset.setToolTipText("Reset");
    btnReset.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnResetActionPerformed(evt);
      }
    });

    btnImport.setFont(btnImport.getFont().deriveFont(btnImport.getFont().getSize()-1f));
    btnImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hu/akusius/palenque/anigifmaker/ui/icons/import.png"))); // NOI18N
    btnImport.setToolTipText("Import");
    btnImport.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnImportActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel16)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(tfDesc, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(2, 2, 2)
        .addComponent(btnImport)
        .addGap(2, 2, 2)
        .addComponent(btnReset)
        .addContainerGap())
    );
    jPanel4Layout.setVerticalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addGap(6, 6, 6)
        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(btnExport)
          .addComponent(btnImport)
          .addComponent(btnReset)
          .addGroup(jPanel4Layout.createSequentialGroup()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(tfDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel16))
            .addGap(1, 1, 1)))
        .addContainerGap())
    );

    add(jPanel4, java.awt.BorderLayout.PAGE_END);
  }// </editor-fold>//GEN-END:initComponents

  private final static String EXTENSION = "pqgp";

  private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
    try {
      AnimParams params = toParams();
      TextImpexerDialog.exportText(UIUtils.windowForComponent(this), params.serialize(), EXTENSION);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Invalid parameters!", "Error", JOptionPane.WARNING_MESSAGE);
    }
  }//GEN-LAST:event_btnExportActionPerformed

  private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
    if (JOptionPane.showConfirmDialog(this, "Do you really want to reset to the defaults?", "Are you sure?",
            JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
      AnimParams params = new AnimParamsBuilder().createAnimParams();
      fromParams(params);
    }
  }//GEN-LAST:event_btnResetActionPerformed

  private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
    try {
      String s = TextImpexerDialog.importText(UIUtils.windowForComponent(this), EXTENSION);
      if (s != null) {
        AnimParams params = AnimParams.deserialize(s);
        fromParams(params);
      }
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Invalid format!", "Error", JOptionPane.WARNING_MESSAGE);
    }
  }//GEN-LAST:event_btnImportActionPerformed

  private void chkAutoCamItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkAutoCamItemStateChanged
    boolean autoCam = chkAutoCam.isSelected();
    spTranslateX.setEnabled(!autoCam);
    spTranslateY.setEnabled(!autoCam);
    spTheta.setEnabled(!autoCam);
    spPhi.setEnabled(!autoCam);
    spZoom.setEnabled(!autoCam);
  }//GEN-LAST:event_chkAutoCamItemStateChanged

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnExport;
  private javax.swing.JButton btnImport;
  private javax.swing.JButton btnReset;
  private javax.swing.JCheckBox chkAutoCam;
  private javax.swing.JCheckBox chkShowGrid;
  private javax.swing.Box.Filler filler1;
  private javax.swing.Box.Filler filler2;
  private javax.swing.Box.Filler filler3;
  private javax.swing.Box.Filler filler4;
  private javax.swing.Box.Filler filler5;
  private javax.swing.JPanel gridPanel;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel11;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel14;
  private javax.swing.JLabel jLabel15;
  private javax.swing.JLabel jLabel16;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JSpinner spFPS;
  private javax.swing.JSpinner spFadeIn;
  private javax.swing.JSpinner spFadeOut;
  private javax.swing.JSpinner spFirstStep;
  private javax.swing.JSpinner spHeight;
  private javax.swing.JSpinner spHoldIn;
  private javax.swing.JSpinner spHoldOut;
  private javax.swing.JSpinner spLastStep;
  private javax.swing.JSpinner spPhi;
  private javax.swing.JSpinner spSpeed;
  private javax.swing.JSpinner spTheta;
  private javax.swing.JSpinner spTranslateX;
  private javax.swing.JSpinner spTranslateY;
  private javax.swing.JSpinner spWidth;
  private javax.swing.JSpinner spZoom;
  private javax.swing.JTextField tfDesc;
  // End of variables declaration//GEN-END:variables
}

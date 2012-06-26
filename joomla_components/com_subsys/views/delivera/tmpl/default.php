<?php defined('_JEXEC') or die('Restricted access'); ?>

<form action="index.php" method="post" name="adminForm" id="adminForm">
<div class="col100">
	<fieldset class="adminform">
		<legend><?php echo JText::_( 'Pls fill in Delivery Details' ); ?></legend>

		<table class="admintable">
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Subscriber' ); ?>:
				</label>
			</td>
			<td>
				<?php 
				$options = array();
 
    $options[] = JHTML::_('select.option','',JText::_('PLS CHOOSE A SUBSCRIBER'));
    //dump($this->subscribers, "SUBS");
    foreach($this->subscribers as $key=>$value) :
      $options[] = JHTML::_('select.option', $value->sub_code, JText::_($value->sub_name.'('.$value->sub_code.')'));
    endforeach;
    $selected = ($this->subcode > 0)? $this->subcode : '';
    echo JHTML::_('select.genericlist', $options, 'sub_code', '', 'value', 'text', $selected);
				?>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Order Code' ); ?>:
				</label>
			</td>
			<td>
    <?php 
				$options = array();
 
    $options[] = JHTML::_('select.option','',JText::_('PLS CHOOSE ORDER CODE'));
    foreach($this->orders as $key=>$value) :   
      $options[] = JHTML::_('select.option', $value->order_code, JText::_($value->order_code));
    endforeach;
    echo JHTML::_('select.genericlist', $options, 'order_code', '', 'value', 'text', '');
				?>
			</td>
		</tr>
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Publication' ); ?>:
				</label>
			</td>
			<td>
				<?php 
				$options = array();
 
    $options[] = JHTML::_('select.option','',JText::_('PLS CHOOSE A PUBLICATION'));
    foreach($this->publications as $key=>$value) :
      ## Create $value ##
      #dump($value, "CATS");
      $options[] = JHTML::_('select.option', $value->pub_code, JText::_($value->pub_name.'('.$value->pub_code.')'));
    endforeach;
    $selectedpub = ($this->pubcode > 0)? $this->pubcode : '';
    echo JHTML::_('select.genericlist', $options, 'pub_code', '', 'value', 'text', $selectedpub);
				?>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">			
			</td>		
		</tr>
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Delivery Date' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="delivery_date" id="delivery_date" size="32" maxlength="250" value="" />
				<input type="reset" class="button" value="..." onclick="return showCalendar('delivery_date','%Y-%m-%d');">
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Delivery Title' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="delivery_title" id="delivery_title" size="32" maxlength="250" value="" />
			</td>
		</tr>
		
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Issue' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="delivery_issue" id="delivery_issue" size="32" maxlength="250" value="" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Issue Date' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="delivery_issuedt" id="delivery_issuedt" size="32" maxlength="250" value="" />
				<input type="reset" class="button" value="..." onclick="return showCalendar('delivery_issuedt','%Y-%m-%d');">
			</td>
		</tr>		
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Copies' ); ?>:
				</label>
			</td>
			<td>
			<input class="textarea" type="text" name="delivery_copies" id="delivery_copies" size="32" maxlength="250" value="" />				
			</td>
		</tr>
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Delivered By' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="delivered_by" id="delivery_by" size="32" maxlength="250" value="" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Received by' ); ?>:
				</label>
			</td>
			<td>
			<input class="textarea" type="text" name="received_by" id="received_by" size="32" maxlength="250" value="" />				
			</td>
		</tr>		
				
	</table>
	</fieldset>
</div>
<div class="clr"></div>

<input type="hidden" name="option" value="com_subsys" />
<input type="hidden" name="task" value="" />
<input type="hidden" name="controller" value="delivery" />
</form>
